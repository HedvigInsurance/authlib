package com.hedvig.authlib

enum class AuthEnvironment {
    STAGING, PRODUCTION
}

val AuthEnvironment.baseUrl: String
    get() {
        return when (this) {
            AuthEnvironment.STAGING -> "https://auth.dev.hedvigit.com"
            AuthEnvironment.PRODUCTION -> "https://auth.prod.hedvigit.com"
        }
    }
