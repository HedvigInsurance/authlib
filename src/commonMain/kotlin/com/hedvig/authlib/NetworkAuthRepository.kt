package com.hedvig.authlib

import com.hedvig.authlib.authservice.AuthService
import com.hedvig.authlib.authservice.model.GrantTokenInput
import com.hedvig.authlib.authservice.model.OtpVerifyResponse
import com.hedvig.authlib.internal.commonKtorConfiguration
import com.hedvig.authlib.network.RevokeRequest
import com.hedvig.authlib.network.buildStartLoginRequest
import com.hedvig.authlib.network.toAuthAttemptResult
import com.hedvig.authlib.network.toSubmitOtpResult
import com.hedvig.authlib.url.OtpResendUrl
import com.hedvig.authlib.url.OtpVerifyUrl
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val POLL_DELAY_MILLIS = 1000L

public class NetworkAuthRepository(
    private val environment: AuthEnvironment,
    private val additionalHttpHeadersProvider: () -> Map<String, String>,
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
    private val authService = AuthService(environment, ktorClient)

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
            when (val response = authService.otpVerify(otp = otp, otpVerifyUrl = OtpVerifyUrl(verifyUrl))) {
                is OtpVerifyResponse.Error -> SubmitOtpResult.Error(response.statusText)
                is OtpVerifyResponse.Success -> SubmitOtpResult.Success(AuthorizationCodeGrant(response.authorizationCode))
            }
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> throw e
                is IOException -> SubmitOtpResult.Error("IO Error with message: ${e.message ?: "unknown message"}")
                is NoTransformationFoundException -> SubmitOtpResult.Error(e.message ?: "unknown error")
                else -> SubmitOtpResult.Error("Error: ${e.message}")
            }
        }
    }

    override suspend fun resendOtp(resendUrl: String): ResendOtpResult {
        return try {
            val succeeded = authService.otpResend(OtpResendUrl(resendUrl))
            if (succeeded) {
                ResendOtpResult.Success
            } else {
                ResendOtpResult.Error("authService.otpResend($resendUrl) resulted in a non 200 response")
            }
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> throw e
                is IOException -> ResendOtpResult.Error("IO Error with message: ${e.message ?: "unknown message"}")
                is NoTransformationFoundException -> ResendOtpResult.Error(e.message ?: "unknown error")
                else -> ResendOtpResult.Error("Error: ${e.message}")
            }
        }
    }

    override suspend fun exchange(grant: Grant): AuthTokenResult {
        val grantTokenInput = when (grant) {
            is AuthorizationCodeGrant -> GrantTokenInput.AuthorizationCode(grant.code)
            is RefreshTokenGrant -> GrantTokenInput.RefreshToken(grant.code)
        }
        return try {
            val response = authService.grantToken(grantTokenInput)
            AuthTokenResult.Success(
                AccessToken(response.accessToken, response.accessTokenExpiresIn),
                RefreshToken(response.refreshToken, response.refreshTokenExpiresIn)
            )
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> throw e
                is IOException -> AuthTokenResult.Error.IOError("IO Error with message: ${e.message ?: "unknown message"}")
                is NoTransformationFoundException -> AuthTokenResult.Error.BackendErrorResponse(e.message ?: "unknown error")
                else -> AuthTokenResult.Error.UnknownError("Error: ${e.message}")
            }
        }
    }

    override suspend fun revoke(token: String): RevokeResult {
        return try {
            val succeeded = authService.revokeToken(token)
            if (succeeded) {
                RevokeResult.Success
            } else {
                RevokeResult.Error("authService.revokeToken($token) resulted in a non 200 response")
            }
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> throw e
                is IOException -> RevokeResult.Error("IO Error with message: ${e.message ?: "unknown message"}")
                is NoTransformationFoundException -> RevokeResult.Error(e.message ?: "unknown error")
                else -> RevokeResult.Error("Error: ${e.message}")
            }
        }
    }
}
