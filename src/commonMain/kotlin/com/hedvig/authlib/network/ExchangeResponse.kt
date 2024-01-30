package com.hedvig.authlib.network

import com.hedvig.authlib.AccessToken
import com.hedvig.authlib.AuthTokenResult
import com.hedvig.authlib.RefreshToken
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ExchangeResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val accessTokenExpiresIn: Int,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("refresh_token_expires_in")
    val refreshTokenExpiresIn: Int
)

internal suspend fun HttpResponse.toAuthTokenResult(): AuthTokenResult {
    return if (status == HttpStatusCode.OK) {
        val response = body<ExchangeResponse>()
        response.toAuthAttemptResult()
    } else {
        AuthTokenResult.Error.BackendErrorResponse(
            status.value,
            "Description:${status.description}. Body as text: ${bodyAsText()}",
        )
    }
}

private fun ExchangeResponse.toAuthAttemptResult(): AuthTokenResult.Success = AuthTokenResult.Success(
    accessToken = AccessToken(
        token = accessToken,
        expiryInSeconds = accessTokenExpiresIn
    ),
    refreshToken = RefreshToken(
        token = refreshToken,
        expiryInSeconds = refreshTokenExpiresIn
    )
)
