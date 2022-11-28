package com.hedvig.authlib.network

import com.hedvig.authlib.AccessToken
import com.hedvig.authlib.AuthTokenResult
import com.hedvig.authlib.RefreshCode
import com.hedvig.authlib.RefreshToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
  val refreshTokenExpiresIn: Int,
)

// TODO

//fun Response.toAuthTokenResult(): AuthTokenResult {
//  val responseBody = body?.string()
//  val result = if (isSuccessful && responseBody != null) {
//    Json.decodeFromString<SubmitAuthorizationCodeResponse>(responseBody).toAuthAttemptResult()
//  } else {
//    AuthTokenResult.Error(message)
//  }
//  return result
//}

private fun SubmitAuthorizationCodeResponse.toAuthAttemptResult() = AuthTokenResult.Success(
  accessToken = AccessToken(
    token = accessToken,
    expiryInSeconds = accessTokenExpiresIn,
  ),
  refreshToken = RefreshToken(
    token = RefreshCode(refreshToken),
    expiryInSeconds = refreshTokenExpiresIn,
  ),
)
