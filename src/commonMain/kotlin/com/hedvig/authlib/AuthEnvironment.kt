package com.hedvig.authlib

enum class AuthEnvironment {
    STAGING, PRODUCTION
}

internal val AuthEnvironment.baseUrl: String
    get() {
        return when (this) {
            AuthEnvironment.STAGING -> "https://auth.dev.hedvigit.com"
            AuthEnvironment.PRODUCTION -> "https://auth.prod.hedvigit.com"
        }
    }

internal val AuthEnvironment.gatewayUrl: String
    get() {
        return when (this) {
            AuthEnvironment.STAGING -> "https://gateway.test.hedvigit.com"
            AuthEnvironment.PRODUCTION -> "https://gateway.hedvig.com"
        }
    }