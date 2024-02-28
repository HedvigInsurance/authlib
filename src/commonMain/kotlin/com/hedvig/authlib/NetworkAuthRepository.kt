package com.hedvig.authlib

import com.hedvig.authlib.internal.commonKtorConfiguration
import com.hedvig.authlib.network.ExchangeAuthorizationCodeRequest
import com.hedvig.authlib.network.ExchangeRefreshTokenRequest
import com.hedvig.authlib.network.RevokeRequest
import com.hedvig.authlib.network.SubmitOtpRequest
import com.hedvig.authlib.network.buildStartLoginRequest
import com.hedvig.authlib.network.toAuthAttemptResult
import com.hedvig.authlib.network.toAuthTokenResult
import com.hedvig.authlib.network.toLoginStatusResult
import com.hedvig.authlib.network.toSubmitOtpResult
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val POLL_DELAY_MILLIS = 1000L

public data class Callbacks(
    val successUrl: String,
    val failureUrl: String
)

public class NetworkAuthRepository(
    private val environment: AuthEnvironment,
    private val additionalHttpHeadersProvider: () -> Map<String, String>,
    private val callbacks: Callbacks,
    private val httpClientEngine: HttpClientEngine? = null,
) : AuthRepository {
    private val ktorClient: HttpClient = run {
        val httpClientConfig: HttpClientConfig<*>.() -> Unit = {
            commonKtorConfiguration(additionalHttpHeadersProvider).invoke(this)
        }
        if (httpClientEngine == null) {
            HttpClient {
                httpClientConfig()
            }
        } else {
            HttpClient(httpClientEngine) {
                httpClientConfig()
            }
        }
    }

    override suspend fun startLoginAttempt(
        loginMethod: LoginMethod,
        market: String,
        personalNumber: String?,
        email: String?
    ): AuthAttemptResult {
        return try {
            val response = ktorClient.post("${environment.baseUrl}/member-login") {
                buildStartLoginRequest(
                    loginMethod,
                    market,
                    personalNumber,
                    email,
                    callbacks
                )
            }

            return response.toAuthAttemptResult()
        } catch (e: IOException) {
            AuthAttemptResult.Error.IOError("IOError: ${e.message}")
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            AuthAttemptResult.Error.UnknownError("Error: ${e.message}")
        }
    }

    override suspend fun loginStatus(statusUrl: StatusUrl): LoginStatusResult {
        return try {
            val response = ktorClient.get("${environment.baseUrl}${statusUrl.url}")
            response.toLoginStatusResult()
        } catch (e: Exception) {
            LoginStatusResult.Exception("Error: ${e.message}")
        }
    }

    override fun observeLoginStatus(statusUrl: StatusUrl): Flow<LoginStatusResult> {
        return flow {
            while (true) {
                val loginStatusResult = loginStatus(statusUrl)

                emit(loginStatusResult)

                if (loginStatusResult is LoginStatusResult.Pending) {
                    delay(POLL_DELAY_MILLIS)
                } else {
                    break
                }
            }
        }
    }

    override suspend fun submitOtp(verifyUrl: String, otp: String): SubmitOtpResult {
        return try {
            val response = ktorClient.post("${environment.baseUrl}$verifyUrl") {
                contentType(ContentType.Application.Json)
                setBody(SubmitOtpRequest(otp))
            }

            response.toSubmitOtpResult()
        } catch (e: Exception) {
            SubmitOtpResult.Error("Error: ${e.message}")
        }
    }

    override suspend fun resendOtp(resendUrl: String): ResendOtpResult {
        return try {
            val response = ktorClient.post("${environment.baseUrl}$resendUrl")

            if (response.status == HttpStatusCode.OK) {
                ResendOtpResult.Success
            } else {
                ResendOtpResult.Error("Error: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            ResendOtpResult.Error("Error: ${e.message}")
        }
    }

    override suspend fun exchange(grant: Grant): AuthTokenResult {
        val submitUrl = "${environment.baseUrl}/oauth/token"

        return try {
            when (grant) {
                is AuthorizationCodeGrant -> {
                    val response = ktorClient.post(submitUrl) {
                        contentType(ContentType.Application.Json)
                        setBody(
                            ExchangeAuthorizationCodeRequest(
                                authorizationCode = grant.code,
                                grantType = "authorization_code"
                            )
                        )
                    }

                    response.toAuthTokenResult()
                }

                is RefreshTokenGrant -> {
                    val response = ktorClient.post(submitUrl) {
                        contentType(ContentType.Application.Json)
                        setBody(
                            ExchangeRefreshTokenRequest(
                                refreshToken = grant.code,
                                grantType = "refresh_token"
                            )
                        )
                    }

                    response.toAuthTokenResult()
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            AuthTokenResult.Error.IOError("IO Error with message: ${e.message ?: "unknown message"}")
        } catch (e: Exception) {
            AuthTokenResult.Error.UnknownError("Error: ${e.message}")
        }
    }

    override suspend fun revoke(token: String): RevokeResult {
        return try {
            val response = ktorClient.post("${environment.baseUrl}/oauth/revoke") {
                contentType(ContentType.Application.Json)
                setBody(RevokeRequest(token))
            }

            if (response.status == HttpStatusCode.OK) {
                RevokeResult.Success
            } else {
                RevokeResult.Error("Could not logout: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            RevokeResult.Error("Error: ${e.message}")
        }
    }
}
