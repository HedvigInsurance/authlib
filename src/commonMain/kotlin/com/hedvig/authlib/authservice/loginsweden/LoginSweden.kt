package com.hedvig.authlib.authservice.loginsweden

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


@Serializable
internal data class LoginSwedenInput(val personalNumber: String?) {
    val method: String = "SE_BANKID"
}

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("result")
@Serializable
internal sealed interface LoginSwedenResponse {
    @Serializable
    @SerialName("success")
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
    @SerialName("error")
    data class Error(val reason: String) : LoginSwedenResponse
}