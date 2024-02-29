package com.hedvig.authlib.authservice.model.otp.swedenlogin

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
internal data class LoginOtpSwedenInput(
    val email: String,
) {
    val method: String = "OTP"
}

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("result")
@Serializable
internal sealed interface LoginOtpSwedenResponse {
    @Serializable
    @SerialName("success")
    data class Success(
        val id: String,
        val statusUrl: String,
        val otpProperties: OtpProperties
    ) : LoginOtpSwedenResponse {
        @Serializable
        data class OtpProperties(
            val resendUrl: String,
            val verifyUrl: String,
            @SerialName("email")
            val maskedEmail: String,
        )
    }

    @Serializable
    @SerialName("error")
    data class Error(val reason: String) : LoginOtpSwedenResponse
}