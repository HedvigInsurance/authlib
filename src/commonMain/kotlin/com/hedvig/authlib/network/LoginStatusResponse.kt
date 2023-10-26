package com.hedvig.authlib.network

import com.hedvig.authlib.LoginStatusResult
import com.hedvig.authlib.AuthorizationCodeGrant
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
internal data class LoginStatusResponse(
    val status: LoginStatus,
    val statusText: String,
    val authorizationCode: String?,
) {
    enum class LoginStatus {
        PENDING, FAILED, COMPLETED
    }
}

internal suspend fun HttpResponse.toLoginStatusResult(): LoginStatusResult {
    val result = if (status == HttpStatusCode.OK) {
        val responseBody = body<LoginStatusResponse>()
        responseBody.toLoginStatusResult()
    } else {
        LoginStatusResult.Exception(message = bodyAsText())
    }
    return result
}

private fun LoginStatusResponse.toLoginStatusResult(): LoginStatusResult = when (status) {
    LoginStatusResponse.LoginStatus.PENDING -> LoginStatusResult.Pending(statusText)
    LoginStatusResponse.LoginStatus.FAILED -> LoginStatusResult.Failed(statusText)
    LoginStatusResponse.LoginStatus.COMPLETED -> {
        require(authorizationCode != null) {
            "Login status completed but did not receive authorization code"
        }

        val code = AuthorizationCodeGrant(authorizationCode)
        LoginStatusResult.Completed(code)
    }
}
