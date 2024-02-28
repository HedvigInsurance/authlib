package com.hedvig.authlib.authservice.otpswedenlogin

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
internal data class LoginOtpSwedenInput(
    val email: String,
) {
    val method: String = "OTP"
}

private class LoginOtpSwedenResponseSerializer :
    JsonContentPolymorphicSerializer<LoginOtpSwedenResponse>(LoginOtpSwedenResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<LoginOtpSwedenResponse> {
        return when {
            "reason" in element.jsonObject -> LoginOtpSwedenResponse.Error.serializer()
            else -> LoginOtpSwedenResponse.Success.serializer()
        }
    }
}

@Serializable(with = LoginOtpSwedenResponseSerializer::class)
internal sealed interface LoginOtpSwedenResponse {
    @Serializable
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
    data class Error(val reason: String) : LoginOtpSwedenResponse
}