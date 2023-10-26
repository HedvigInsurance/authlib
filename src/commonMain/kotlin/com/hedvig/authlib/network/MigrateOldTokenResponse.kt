package com.hedvig.authlib.network

import kotlinx.serialization.Serializable

@Serializable
internal data class MigrateOldTokenResponse(
    val authorizationCode: String
)
