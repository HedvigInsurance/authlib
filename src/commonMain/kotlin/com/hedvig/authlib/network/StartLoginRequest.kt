package com.hedvig.authlib.network

import com.hedvig.authlib.LoginMethod
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class StartLoginRequest(
    val method: String,
    val country: String,
    val personalNumber: String?,
    val email: String?
)

fun HttpRequestBuilder.buildStartLoginRequest(
    loginMethod: LoginMethod,
    market: String,
    personalNumber: String?,
    email: String?
) {
    val body = StartLoginRequest(
        loginMethod.name,
        market,
        personalNumber,
        email
    )

    contentType(ContentType.Application.Json)
    setBody(body)
}
