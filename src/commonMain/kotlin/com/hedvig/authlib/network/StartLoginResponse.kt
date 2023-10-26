package com.hedvig.authlib.network

import com.hedvig.authlib.AuthAttemptResult
import com.hedvig.authlib.StatusUrl
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
internal data class StartLoginResponse(
    val id: String,
    val method: String,
    val statusUrl: String,
    val seBankIdProperties: BankIdProperties?,
    val zignSecProperties: ZignSecProperties?,
    val otpProperties: OtpProperties?
) {
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
        val verifyUrl: String
    )
}

internal suspend fun HttpResponse.toAuthAttemptResult(): AuthAttemptResult {
    return if (status == HttpStatusCode.OK) {
        body<StartLoginResponse>().toAuthAttemptResult()
    } else {
        AuthAttemptResult.Error(message = bodyAsText())
    }
}

private fun StartLoginResponse.toAuthAttemptResult() = when {
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
        verifyUrl = otpProperties.verifyUrl
    )
    else -> AuthAttemptResult.Error(
        message = "Could not find properties on start login response"
    )
}
