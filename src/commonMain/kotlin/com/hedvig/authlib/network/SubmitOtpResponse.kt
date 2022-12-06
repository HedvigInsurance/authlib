package com.hedvig.authlib.network

import kotlinx.serialization.Serializable

@Serializable
data class SubmitOtpResponse(
  val authorizationCode: String,
)

// TODO

//fun Response.toSubmitOtpResult(): SubmitOtpResult {
//  val responseBody = body?.string()
//  val result = if (isSuccessful && responseBody != null) {
//    val response = Json.decodeFromString<SubmitOtpResponse>(responseBody)
//    SubmitOtpResult.Success(LoginAuthorizationCode(response.authorizationCode))
//  } else {
//    SubmitOtpResult.Error(message)
//  }
//  return result
//}
