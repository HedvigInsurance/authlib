package com.hedvig.authlib.connectpayment

import kotlinx.serialization.Serializable

@Serializable
internal data class MemberAuthorizationCodesResponse(
    val authorizationCode: String,
)