package com.hedvig.authlib.network

import com.hedvig.authlib.LoginMethod
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
internal data class StartLoginRequest(
    val method: String,
    val country: String,
    val personalNumber: String?,
    val email: String?,
)

internal fun HttpRequestBuilder.buildStartLoginRequest(
    loginMethod: LoginMethod,
    market: String,
    personalNumber: String?,
    email: String?,
) {
    contentType(ContentType.Application.Json)
    setBody(
        StartLoginRequest(
            method = loginMethod.name,
            country = market,
            personalNumber = personalNumber,
            email = email,
        )
    )
    if (loginMethod == LoginMethod.SE_BANKID) {
        headers["hedvig-bankid-v6"] = "true"
    }
}
