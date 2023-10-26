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

    public suspend fun migrateOldToken(token: String): AuthTokenResult

    /**
     * Returns the member's authorization code which can be used to start a connect-payment process
     * for NO and DK
     *
     * [webLocale] The locale string that the web uses to represent each locale.
     *             Input may look like "se", "se-en", "no-en" etc.
     */
    public suspend fun getMemberAuthorizationCode(webLocale: String): MemberAuthorizationCodeResult
}

@Suppress("unused")
public enum class LoginMethod {
    SE_BANKID, ZIGNSEC, OTP
}

public sealed interface AuthAttemptResult {

    public data class Error(
        val message: String
    ) : AuthAttemptResult

    public data class BankIdProperties(
        val id: String,
        val statusUrl: StatusUrl,
        val autoStartToken: String
    ) : AuthAttemptResult

    public data class ZignSecProperties(
        val id: String,
        val statusUrl: StatusUrl,
        val redirectUrl: String
    ) : AuthAttemptResult

    public data class OtpProperties(
        val id: String,
        val statusUrl: StatusUrl,
        val resendUrl: String,
        val verifyUrl: String
    ) : AuthAttemptResult
}

public data class StatusUrl(val url: String)

public sealed interface AuthTokenResult {
    public data class Error(val message: String) : AuthTokenResult

    public data class Success(
        val accessToken: AccessToken,
        val refreshToken: RefreshToken
    ) : AuthTokenResult
}

public sealed interface LoginStatusResult {
    public data class Exception(val message: String) : LoginStatusResult
    public data class Failed(val message: String) : LoginStatusResult
    public data class Pending(val statusMessage: String) : LoginStatusResult
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
