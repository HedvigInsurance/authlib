package com.hedvig.authlib.network

import com.hedvig.authlib.Callbacks
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
    val callbackSuccess: String,
    val callbackFailure: String
)

internal fun HttpRequestBuilder.buildStartLoginRequest(
    loginMethod: LoginMethod,
    market: String,
    personalNumber: String?,
    email: String?,
    callbacks: Callbacks
) {
    val body = StartLoginRequest(
        loginMethod.name,
        market,
        personalNumber,
        email,
        callbacks.successUrl,
        callbacks.failureUrl
    )

    contentType(ContentType.Application.Json)
    setBody(body)
}
