package com.hedvig.authlib

import com.hedvig.authlib.url.LoginStatusUrl
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmInline

public interface AuthRepository {
    public suspend fun startLoginAttempt(
        loginMethod: LoginMethod,
        market: OtpMarket,
        personalNumber: String? = null,
        email: String? = null
    ): AuthAttemptResult

    public fun observeLoginStatus(statusUrl: LoginStatusUrl): Flow<LoginStatusResult>

    public suspend fun loginStatus(statusUrl: LoginStatusUrl): LoginStatusResult

    public suspend fun submitOtp(verifyUrl: String, otp: String): SubmitOtpResult

    public suspend fun resendOtp(resendUrl: String): ResendOtpResult

    public suspend fun exchange(grant: Grant): AuthTokenResult

    public suspend fun revoke(token: String): RevokeResult
}

public enum class LoginMethod {
    SE_BANKID, OTP
}

public enum class OtpMarket {
    SE, NO, DK
}

public sealed interface AuthAttemptResult {

    public sealed interface Error : AuthAttemptResult {
        public data class Localised(val reason: String) : Error
        public data class BackendErrorResponse(val message: String) : Error
        public data class IOError(val message: String) : Error
        public data class UnknownError(val message: String) : Error
    }

    public data class BankIdProperties(
        val id: String,
        val statusUrl: LoginStatusUrl,
        val autoStartToken: String,
        val liveQrCodeData: String,
        val bankIdOpened: Boolean,
    ) : AuthAttemptResult

    public data class OtpProperties(
        val id: String,
        val statusUrl: LoginStatusUrl,
        val resendUrl: String,
        val verifyUrl: String,
        val maskedEmail: String?,
    ) : AuthAttemptResult
}

public sealed interface AuthTokenResult {
    public sealed interface Error: AuthTokenResult {
        public data class BackendErrorResponse(val message: String) : Error
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
    val expiryInSeconds: Long
)

public data class RefreshToken(
    val token: String,
    val expiryInSeconds: Long
)

public sealed interface RevokeResult {
    public data class Error(val message: String) : RevokeResult
    public data object Success : RevokeResult
}
