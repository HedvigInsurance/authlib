package com.hedvig.authlib.authservice.loginotp

import com.hedvig.authlib.url.OtpResendUrl
import com.hedvig.authlib.url.OtpVerifyUrl
import com.hedvig.authlib.url.LoginStatusUrl
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
internal data class LoginOtpInput(
    val country: OtpLoginCountry,
    val personalNumber: String,
) {
    val method: String = "OTP"

    enum class OtpLoginCountry {
        NO, DK
    }
}

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("result")
@Serializable
internal sealed interface LoginOtpResponse {
    @Serializable
    @SerialName("success")
    data class Success(
        val id: String,
        val statusUrl: LoginStatusUrl,
        val otpProperties: OtpProperties
    ) : LoginOtpResponse {
        @Serializable
        data class OtpProperties(
            val resendUrl: OtpResendUrl,
            val verifyUrl: OtpVerifyUrl,
            @SerialName("email")
            val maskedEmail: String,
        )
    }

    @Serializable
    @SerialName("error")
    data class Error(val reason: String) : LoginOtpResponse
}