package com.hedvig.authlib

import com.hedvig.authlib.network.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val POLL_DELAY_MILLIS = 1000L

class NetworkAuthRepository(
    private val ktorClient: HttpClient,
    private val url: String
) : AuthRepository {
    override suspend fun startLoginAttempt(
        loginMethod: LoginMethod,
        market: String,
        personalNumber: String?,
        email: String?
    ): AuthAttemptResult {
        return try {
            val response = ktorClient.post("$url/member-login") {
                buildStartLoginRequest(
                    loginMethod,
                    market,
                    personalNumber,
                    email
                )
            }

            return response.toAuthAttemptResult()
        } catch (e: Exception) {
            AuthAttemptResult.Error("Error: ${e.message}")
        }
    }

    override fun observeLoginStatus(statusUrl: StatusUrl): Flow<LoginStatusResult> {
        return flow {
            while (true) {
                try {
                    val response = ktorClient.get("$url${statusUrl.url}")

                    val loginStatusResult = response.toLoginStatusResult()

                    emit(loginStatusResult)

                    if (loginStatusResult is LoginStatusResult.Pending) {
                        delay(POLL_DELAY_MILLIS)
                    } else {
                        break
                    }
                } catch (e: Exception) {
                    emit(LoginStatusResult.Failed("Error: ${e.message}"))
                }
            }
        }
    }

    override suspend fun submitOtp(verifyUrl: String, otp: String): SubmitOtpResult {
        val response = ktorClient.post("$url$verifyUrl") {
            contentType(ContentType.Application.Json)
            setBody(SubmitOtpRequest(otp))
        }

        return response.body()
    }

    override suspend fun resendOtp(resendUrl: String): ResendOtpResult {
        return try {
            val response = ktorClient.post("$url$resendUrl")

            return response.body()
        } catch (e: Exception) {
            ResendOtpResult.Error("Error: ${e.message}")
        }
    }

    override suspend fun submitAuthorizationCode(authorizationCode: AuthorizationCode): AuthTokenResult {
        val submitUrl = "$url/oauth/token"

        return try {
            when (authorizationCode) {
                is LoginAuthorizationCode -> {
                    val response = ktorClient.post(submitUrl) {
                        contentType(ContentType.Application.Json)
                        setBody(
                            SubmitAuthorizationCodeRequest(
                                authorizationCode = authorizationCode.code,
                                grantType = "authorization_code"
                            )
                        )
                    }

                    response.toAuthTokenResult()
                }

                is RefreshCode -> {
                    val response = ktorClient.post(submitUrl) {
                        contentType(ContentType.Application.Json)
                        setBody(
                            SubmitAuthorizationCodeRequest(
                                authorizationCode = authorizationCode.code,
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

    override suspend fun logout(refreshCode: RefreshCode): LogoutResult {
        return try {
            val response = ktorClient.post("$url/oauth/logout") {
                contentType(ContentType.Application.Json)
                setBody(LogoutRequest(refreshCode.code))
            }

            if (response.status == HttpStatusCode.OK) {
                LogoutResult.Success
            } else {
                LogoutResult.Error("Could not logout: ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            LogoutResult.Error("Error: ${e.message}")
        }
    }
}
