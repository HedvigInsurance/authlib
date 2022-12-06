package com.hedvig.authlib.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RevokeRequest(
    val token: String
)