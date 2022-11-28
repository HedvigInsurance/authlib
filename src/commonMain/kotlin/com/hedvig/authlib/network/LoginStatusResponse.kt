package com.hedvig.authlib.network

import com.hedvig.authlib.LoginAuthorizationCode
import com.hedvig.authlib.LoginStatusResult
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import kotlinx.serialization.Serializable

@Serializable
data class LoginStatusResponse(
    val status: LoginStatus,
    val seBankidHintCode: String?,
    val authorizationCode: String?,
) {
  enum class LoginStatus {
    PENDING, FAILED, COMPLETED
  }
}

suspend fun HttpResponse.toLoginStatusResult(): LoginStatusResult {
  val responseBody = bodyAsText()

  val result = if (status == HttpStatusCode.OK) {
    LoginStatusResult.Failed(message = "TODO")
    // Json.decodeFromString<LoginStatusResponse>(responseBody).toLoginStatusResult() TODO
  } else {
    LoginStatusResult.Failed(message = "TODO")
  }
  return result
}

private fun LoginStatusResponse.toLoginStatusResult() = when (status) {
  LoginStatusResponse.LoginStatus.PENDING -> LoginStatusResult.Pending(seBankidHintCode)
  LoginStatusResponse.LoginStatus.FAILED -> LoginStatusResult.Failed("Login status failed")
  LoginStatusResponse.LoginStatus.COMPLETED -> {
    require(authorizationCode != null) {
      "Login status completed but did not receive authorization code"
    }

    val code = LoginAuthorizationCode(authorizationCode)
    LoginStatusResult.Completed(code)
  }
}
