package com.hedvig.authlib.authservice.model

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginOtpSwedenInput(val email: String) {
    val method: String = "OTP"
}
