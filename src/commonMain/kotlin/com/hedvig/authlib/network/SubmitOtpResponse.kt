package com.hedvig.authlib.network

import com.hedvig.authlib.AuthorizationCodeGrant
import com.hedvig.authlib.SubmitOtpResult
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
internal data class SubmitOtpResponse(
    val authorizationCode: String
)

internal suspend fun HttpResponse.toSubmitOtpResult(): SubmitOtpResult {
    return if (status == HttpStatusCode.OK) {
        val response = body<SubmitOtpResponse>()
        SubmitOtpResult.Success(AuthorizationCodeGrant(response.authorizationCode))
    } else {
        SubmitOtpResult.Error(bodyAsText())
    }
}
