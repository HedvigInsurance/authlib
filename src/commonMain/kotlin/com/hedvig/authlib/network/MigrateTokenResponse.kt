package com.hedvig.authlib.network

import kotlinx.serialization.Serializable

@Serializable
data class MigrateTokenResponse(
    val token: String
)
