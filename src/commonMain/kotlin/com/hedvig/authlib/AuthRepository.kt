package com.hedvig.authlib

import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

public interface AuthRepository {
    public suspend fun startLoginAttempt(
        loginMethod: LoginMethod,
        market: String,
        personalNumber: String? = null,
        email: String? = null
    ): AuthAttemptResult

    public fun observeLoginStatus(statusUrl: StatusUrl): Flow<LoginStatusResult>

    public suspend fun loginStatus(statusUrl: StatusUrl): LoginStatusResult

    public suspend fun submitOtp(verifyUrl: String, otp: String): SubmitOtpResult

    public suspend fun resendOtp(resendUrl: String): ResendOtpResult

    public suspend fun exchange(grant: Grant): AuthTokenResult

    public suspend fun revoke(token: String): RevokeResult
}

@Suppress("unused")
public enum class LoginMethod {
    SE_BANKID, OTP
}

public sealed interface AuthAttemptResult {

    public sealed interface Error : AuthAttemptResult {
        public data class Localised(val reason: String) : Error
        public data class BackendErrorResponse(val message: String, val httpStatusValue: Int) : Error
        public data class IOError(val message: String) : Error
        public data class UnknownError(val message: String) : Error
    }

    public data class BankIdProperties(
        val id: String,
        val statusUrl: StatusUrl,
        val autoStartToken: String,
        val liveQrCodeData: String
    ) : AuthAttemptResult

    public data class OtpProperties(
        val id: String,
        val statusUrl: StatusUrl,
        val resendUrl: String,
        val verifyUrl: String,
        val maskedEmail: String?,
    ) : AuthAttemptResult
}

public sealed interface AuthTokenResult {
    public sealed interface Error: AuthTokenResult {
        public data class BackendErrorResponse(val message: String, val httpStatusValue: Int) : Error
        public data class IOError(val message: String) : Error
        public data class UnknownError(val message: String) : Error
    }

    public data class Success(
        val accessToken: AccessToken,
        val refreshToken: RefreshToken
    ) : AuthTokenResult
}

public sealed interface LoginStatusResult {
    public data class Exception(val message: String) : LoginStatusResult
    public data class Failed(val message: String) : LoginStatusResult
    public data class Pending(val statusMessage: String, val liveQrCodeData: String?) : LoginStatusResult
    public data class Completed(val authorizationCode: AuthorizationCodeGrant) : LoginStatusResult
}

public sealed interface SubmitOtpResult {
    public data class Error(val message: String) : SubmitOtpResult
    public data class Success(val loginAuthorizationCode: AuthorizationCodeGrant) : SubmitOtpResult
}

public sealed interface ResendOtpResult {
    public data class Error(val message: String) : ResendOtpResult
    public data object Success : ResendOtpResult
}

public sealed interface Grant {
    public val code: String
}

public data class AuthorizationCodeGrant(override val code: String) : Grant

public data class RefreshTokenGrant(override val code: String) : Grant

public data class AccessToken(
    val token: String,
    val expiryInSeconds: Int
)

public data class RefreshToken(
    val token: String,
    val expiryInSeconds: Int
)

public sealed interface RevokeResult {
    public data class Error(val message: String) : RevokeResult
    public data object Success : RevokeResult
}

public sealed interface MemberAuthorizationCodeResult {
    public data class Error(val error: Throwable) : MemberAuthorizationCodeResult

    public data class Success(
        public val memberPaymentUrl: MemberPaymentUrl,
    ) : MemberAuthorizationCodeResult
}

@JvmInline
public value class MemberPaymentUrl(
    public val url: String
)
