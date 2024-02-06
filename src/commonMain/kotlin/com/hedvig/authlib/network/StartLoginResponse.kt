package com.hedvig.authlib.network

import com.hedvig.authlib.AuthAttemptResult
import com.hedvig.authlib.StatusUrl
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface StartLoginResponse {
    @Serializable
    data class Failure(val reason: String) : StartLoginResponse

    @Serializable
    data class Success(
        val id: String,
        val method: String,
        val statusUrl: String,
        val seBankIdProperties: BankIdProperties?,
        val zignSecProperties: ZignSecProperties?,
        val otpProperties: OtpProperties?
    ) : StartLoginResponse {
        @Serializable
        data class BankIdProperties(
            val orderRef: String,
            val autoStartToken: String
        )

        @Serializable
        data class ZignSecProperties(
            val redirectUrl: String
        )

        @Serializable
        data class OtpProperties(
            val resendUrl: String,
            val verifyUrl: String,
            @SerialName("email")
            val maskedEmail: String?,
        )
    }
}

internal suspend fun HttpResponse.toAuthAttemptResult(): AuthAttemptResult {
    return if (status == HttpStatusCode.OK) {
        body<StartLoginResponse>().toAuthAttemptResult()
    } else {
        AuthAttemptResult.Error.BackendErrorResponse(message = bodyAsText(), httpStatusValue = status.value)
    }
}

private fun StartLoginResponse.toAuthAttemptResult(): AuthAttemptResult = when(this) {
    is StartLoginResponse.Failure -> {
        AuthAttemptResult.Error.Localised(this.reason)
    }

    is StartLoginResponse.Success -> {
        when {
            seBankIdProperties != null -> AuthAttemptResult.BankIdProperties(
                id = id,
                statusUrl = StatusUrl(statusUrl),
                autoStartToken = seBankIdProperties.autoStartToken
            )

            zignSecProperties != null -> AuthAttemptResult.ZignSecProperties(
                id = id,
                statusUrl = StatusUrl(statusUrl),
                redirectUrl = zignSecProperties.redirectUrl
            )

            otpProperties != null -> AuthAttemptResult.OtpProperties(
                id = id,
                statusUrl = StatusUrl(statusUrl),
                resendUrl = otpProperties.resendUrl,
                verifyUrl = otpProperties.verifyUrl,
                maskedEmail = otpProperties.maskedEmail,
            )

            else -> AuthAttemptResult.Error.UnknownError(
                message = "Could not find properties on start login response"
            )
        }
    }
}
