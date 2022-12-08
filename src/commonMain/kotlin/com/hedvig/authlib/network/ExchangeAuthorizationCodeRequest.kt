package com.hedvig.authlib.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeAuthorizationCodeRequest(
    @SerialName("authorization_code")
    val authorizationCode: String,
    @SerialName("grant_type")
    val grantType: String
)