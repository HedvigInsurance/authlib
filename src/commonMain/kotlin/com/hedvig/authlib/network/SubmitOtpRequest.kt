package com.hedvig.authlib.network

import kotlinx.serialization.Serializable

@Serializable
data class SubmitOtpRequest(
    val otp: String
)
