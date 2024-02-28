package com.hedvig.authlib.authservice.otplogin

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

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

private class LoginOtpResponseSerializer :
    JsonContentPolymorphicSerializer<LoginOtpResponse>(LoginOtpResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<LoginOtpResponse> {
        return when {
            "reason" in element.jsonObject -> LoginOtpResponse.Error.serializer()
            else -> LoginOtpResponse.Success.serializer()
        }
    }
}

@Serializable(with = LoginOtpResponseSerializer::class)
internal sealed interface LoginOtpResponse {
    @Serializable
    data class Success(
        val id: String,
        val statusUrl: String,
        val otpProperties: OtpProperties
    ) : LoginOtpResponse {
        @Serializable
        data class OtpProperties(
            val resendUrl: String,
            val verifyUrl: String,
            @SerialName("email")
            val maskedEmail: String,
        )
    }

    @Serializable
    data class Error(val reason: String) : LoginOtpResponse
}