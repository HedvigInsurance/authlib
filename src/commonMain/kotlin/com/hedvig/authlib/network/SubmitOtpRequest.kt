package com.hedvig.authlib.network

import kotlinx.serialization.Serializable

@Serializable
internal data class SubmitOtpRequest(
    val otp: String
)
