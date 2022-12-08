package com.hedvig.authlib.network

import com.hedvig.authlib.LoginMethod
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

const val SUCCESS_CALLBACK_URL = "https://hedvig.com?q=success"
const val FAILURE_CALLBACK_URL = "https://hedvig.com?q=failure"

@Serializable
data class StartLoginRequest(
    val method: String,
    val country: String,
    val personalNumber: String?,
    val email: String?,
    val callbackSuccess: String,
    val callbackFailure: String
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
        email,
        SUCCESS_CALLBACK_URL,
        FAILURE_CALLBACK_URL
    )

    contentType(ContentType.Application.Json)
    setBody(body)
}
