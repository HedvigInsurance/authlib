package com.hedvig.authlib.network

import com.hedvig.authlib.AccessToken
import com.hedvig.authlib.AuthTokenResult
import com.hedvig.authlib.RefreshToken
import com.hedvig.authlib.RefreshTokenGrant
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitAuthorizationCodeResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val accessTokenExpiresIn: Int,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("refresh_token_expires_in")
    val refreshTokenExpiresIn: Int
)

suspend fun HttpResponse.toAuthTokenResult(): AuthTokenResult {
    val result = if (status == HttpStatusCode.OK) {
        val response = body<SubmitAuthorizationCodeResponse>()
        return response.toAuthAttemptResult()
    } else {
        AuthTokenResult.Error(bodyAsText())
    }
    return result
}

private fun SubmitAuthorizationCodeResponse.toAuthAttemptResult() = AuthTokenResult.Success(
    accessToken = AccessToken(
        token = accessToken,
        expiryInSeconds = accessTokenExpiresIn
    ),
    refreshToken = RefreshToken(
        token = refreshToken,
        expiryInSeconds = refreshTokenExpiresIn
    )
)
