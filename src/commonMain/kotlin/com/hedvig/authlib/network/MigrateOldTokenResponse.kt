package com.hedvig.authlib.network

import kotlinx.serialization.Serializable

@Serializable
data class MigrateOldTokenResponse(
    val authorizationCode: String
)
