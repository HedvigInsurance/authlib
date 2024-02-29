package com.hedvig.authlib.authservice.model

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginOtpInput(
    val country: OtpLoginCountry,
    val personalNumber: String,
) {
    val method: String = "OTP"

    enum class OtpLoginCountry {
        NO, DK
    }
}
