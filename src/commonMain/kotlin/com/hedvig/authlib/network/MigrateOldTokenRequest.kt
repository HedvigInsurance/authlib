package com.hedvig.authlib.network

import kotlinx.serialization.Serializable

@Serializable
data class MigrateOldTokenRequest(
    val token: String
)