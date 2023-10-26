package com.hedvig.authlib.network

import kotlinx.serialization.Serializable

@Serializable
internal data class MigrateOldTokenRequest(
    val token: String
)
