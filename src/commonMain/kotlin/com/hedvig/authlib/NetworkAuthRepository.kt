package com.hedvig.authlib

import com.hedvig.authlib.network.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

private const val POLL_DELAY_MILLIS = 1000L

data class Callbacks(
    val successUrl: String,
    val failureUrl: String
)

class NetworkAuthRepository(
    private val environment: AuthEnvironment,
    private val additionalHttpHeaders: Map<String, String>,
    private val callbacks: Callbacks
) : AuthRepository {
    private val ktorClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    allowSpecialFloatingPointValues = true
                    isLenient = true
                    allowStructuredMapKeys = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 1000
        }
        defaultRequest {
            additionalHttpHeaders.forEach { entry ->
                header(entry.key, entry.value)
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
        } catch (e: Exception) {
            AuthAttemptResult.Error("Error: ${e.message}")
        }
    }

    override suspend fun loginStatus(statusUrl: StatusUrl): LoginStatusResult {
        return try {
            val response = ktorClient.get("${environment.baseUrl}${statusUrl.url}")
            response.toLoginStatusResult()
        } catch (e: Exception) {
            LoginStatusResult.Failed("Error: ${e.message}")
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
        } catch (e: Exception) {
            AuthTokenResult.Error("Error: ${e.message}")
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

    override suspend fun migrateOldToken(token: String): AuthTokenResult {
        return try {
            val response = ktorClient.post("${environment.gatewayUrl}/migrate-auth-token") {
                contentType(ContentType.Application.Json)
                setBody(MigrateOldTokenRequest(token))
            }

            val responseBody = response.body<MigrateOldTokenResponse>()

            return exchange(AuthorizationCodeGrant(responseBody.authorizationCode))
        } catch (e: Exception) {
            AuthTokenResult.Error("Error: ${e.message}")
        }
    }
}
