package com.hedvig.authlib.network

import com.hedvig.authlib.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class MigrateTokenResponse(
    val authorizationCode: String
)

suspend fun HttpResponse.toMigrateTokenResponse(): MigrateResult {
    return if (status == HttpStatusCode.OK) {
        val response = body<MigrateTokenResponse>()
        response.toMigrateResult()
    } else {
        MigrateResult.Error(bodyAsText())
    }
}

private fun MigrateTokenResponse.toMigrateResult() = MigrateResult.Success(
    grant = AuthorizationCodeGrant(this.authorizationCode)
)
