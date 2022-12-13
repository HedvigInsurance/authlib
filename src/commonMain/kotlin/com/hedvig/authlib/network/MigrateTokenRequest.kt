package com.hedvig.authlib.network

import kotlinx.serialization.Serializable

@Serializable
data class MigrateTokenRequest(
    val token: String
)
