package com.hedvig.authlib

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

interface AuthRepository {
    suspend fun startLoginAttempt(
        loginMethod: LoginMethod,
        market: String,
        personalNumber: String? = null,
        email: String? = null
    ): AuthAttemptResult

    fun observeLoginStatus(statusUrl: StatusUrl): Flow<LoginStatusResult>

    suspend fun loginStatus(statusUrl: StatusUrl): LoginStatusResult

    suspend fun submitOtp(verifyUrl: String, otp: String): SubmitOtpResult

    suspend fun resendOtp(resendUrl: String): ResendOtpResult

    suspend fun exchange(grant: Grant): AuthTokenResult

    suspend fun revoke(token: String): RevokeResult

    suspend fun migrateOldToken(token: String): AuthTokenResult
}

@Suppress("unused")
enum class LoginMethod {
    SE_BANKID, ZIGNSEC, OTP
}

sealed interface AuthAttemptResult {

    data class Error(
        val message: String
    ) : AuthAttemptResult

    data class BankIdProperties(
        val id: String,
        val statusUrl: StatusUrl,
        val autoStartToken: String
    ) : AuthAttemptResult

    data class ZignSecProperties(
        val id: String,
        val statusUrl: StatusUrl,
        val redirectUrl: String
    ) : AuthAttemptResult

    data class OtpProperties(
        val id: String,
        val statusUrl: StatusUrl,
        val resendUrl: String,
        val verifyUrl: String
    ) : AuthAttemptResult
}

data class StatusUrl(val url: String)

sealed interface AuthTokenResult {
    @Serializable
    data class Error(val message: String) : AuthTokenResult

    @Serializable
    data class Success(
        val accessToken: AccessToken,
        val refreshToken: RefreshToken
    ) : AuthTokenResult
}

sealed interface LoginStatusResult {
    data class Exception(val message: String) : LoginStatusResult
    data class Failed(val message: String) : LoginStatusResult
    data class Pending(val statusMessage: String) : LoginStatusResult
    data class Completed(val authorizationCode: AuthorizationCodeGrant) : LoginStatusResult
}

sealed interface SubmitOtpResult {
    data class Error(val message: String) : SubmitOtpResult
    data class Success(val loginAuthorizationCode: AuthorizationCodeGrant) : SubmitOtpResult
}

sealed interface ResendOtpResult {
    data class Error(val message: String) : ResendOtpResult
    object Success : ResendOtpResult
}

sealed interface Grant {
    val code: String
}

data class AuthorizationCodeGrant(override val code: String) : Grant

@Serializable
data class RefreshTokenGrant(override val code: String) : Grant

@Serializable
data class AccessToken(
    val token: String,
    val expiryInSeconds: Int
)

@Serializable
data class RefreshToken(
    val token: String,
    val expiryInSeconds: Int
)

sealed interface RevokeResult {
    data class Error(val message: String) : RevokeResult
    object Success : RevokeResult
}
