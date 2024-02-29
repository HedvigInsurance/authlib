package com.hedvig.authlib.network

import com.hedvig.authlib.AuthAttemptResult
import com.hedvig.authlib.StatusUrl
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface StartLoginResponse {
    @Serializable
    data class Failure(val reason: String) : StartLoginResponse

    @Serializable
    data class Success(
        val id: String,
        val method: String,
        val statusUrl: String,
        val seBankIdProperties: BankIdProperties?,
        val otpProperties: OtpProperties?
    ) : StartLoginResponse {
        @Serializable
        data class BankIdProperties(
            val autoStartToken: String,
            val liveQrCodeData: String
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
        val startLoginSuccess = try {
            body<StartLoginResponse.Success>()
        } catch (_: JsonConvertException) {
            null
        }
        if (startLoginSuccess != null) {
            return startLoginSuccess.toAuthAttemptResult()
        }
        val startLoginFailure = try {
            body<StartLoginResponse.Failure>()
        } catch (_: JsonConvertException) {
            null
        }
        if (startLoginFailure != null) {
            println("Authlib returning error localised:$startLoginFailure")
            return AuthAttemptResult.Error.Localised(startLoginFailure.reason)
        }
        AuthAttemptResult.Error.UnknownError(
            "Login attempt failed, backend response could not be mapped to StartLoginResponse"
        )
    } else {
        AuthAttemptResult.Error.BackendErrorResponse(message = bodyAsText(), httpStatusValue = status.value)
    }
}

private fun StartLoginResponse.Success.toAuthAttemptResult(): AuthAttemptResult = when {
    seBankIdProperties != null -> AuthAttemptResult.BankIdProperties(
        id = id,
        statusUrl = StatusUrl(statusUrl),
        autoStartToken = seBankIdProperties.autoStartToken,
        liveQrCodeData = seBankIdProperties.liveQrCodeData
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
