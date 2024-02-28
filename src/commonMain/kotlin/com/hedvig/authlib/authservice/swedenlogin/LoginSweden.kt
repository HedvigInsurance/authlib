package com.hedvig.authlib.authservice.swedenlogin

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
internal data class LoginSwedenInput(
    val personalNumber: String?
) {
    val method: String = "SE_BANKID"
}


private class LoginSwedenResponseSerializer :
    JsonContentPolymorphicSerializer<LoginSwedenResponse>(LoginSwedenResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<LoginSwedenResponse> {
        return when {
            "reason" in element.jsonObject -> LoginSwedenResponse.Error.serializer()
            else -> LoginSwedenResponse.Success.serializer()
        }
    }
}

@Serializable(with = LoginSwedenResponseSerializer::class)
internal sealed interface LoginSwedenResponse {
    @Serializable
    data class Success(
        val id: String,
        val statusUrl: String,
        val seBankIdProperties: SeBankIdProperties
    ) : LoginSwedenResponse {
        @Serializable
        data class SeBankIdProperties(
            val orderRef: String,
            val autoStartToken: String,
            val liveQrCodeData: String,
            val bankIdAppOpened: String,
        )
    }

    @Serializable
    data class Error(val reason: String) : LoginSwedenResponse
}