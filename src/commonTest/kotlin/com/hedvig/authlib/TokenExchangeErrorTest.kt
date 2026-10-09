package com.hedvig.authlib

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * How `exchange` classifies a failure decides whether a caller keeps the session: only `IOError`
 * is treated as retryable, everything else ends it. These pin that mapping down.
 */
class TokenExchangeErrorTest {

    @Test
    fun transportFailureIsReportedAsIOError() = runTest {
        // What Ktor's Darwin engine throws when the phone is offline: DarwinHttpRequestException
        // extends IOException, so losing signal must not be mistaken for a bad token.
        val result = exchangeRefreshToken(
            MockEngine { throw IOException("Exception in http request: NSURLErrorNotConnectedToInternet") }
        )

        val error = assertIs<AuthTokenResult.Error.IOError>(result)
        assertTrue(error.message.contains("Exception in http request"), error.message)
    }

    @Test
    fun rejectedRefreshTokenIsReportedAsBackendError() = runTest {
        // `auth` answers 403 with its framework error body, which carries none of the token fields.
        val result = exchangeRefreshToken(
            MockEngine { respondJson(HttpStatusCode.Forbidden, """{"status":403,"error":"Forbidden"}""") }
        )

        val error = assertIs<AuthTokenResult.Error.BackendErrorResponse>(result)
        assertTrue(error.message.contains("403"), error.message)
    }

    @Test
    fun serverErrorIsReportedAsIOErrorSoTheSessionSurvives() = runTest {
        // A 5xx is the server being unwell, not the token being bad.
        val result = exchangeRefreshToken(
            MockEngine { respondJson(HttpStatusCode.ServiceUnavailable, """{"status":503}""") }
        )

        assertIs<AuthTokenResult.Error.IOError>(result)
    }

    @Test
    fun successfulExchangeReturnsBothTokens() = runTest {
        val result = exchangeRefreshToken(
            MockEngine {
                respondJson(
                    HttpStatusCode.OK,
                    """
                    {"token_type":"Bearer","access_token":"access","expires_in":900,
                     "refresh_token":"refresh","refresh_token_expires_in":7776000}
                    """.trimIndent()
                )
            }
        )

        val success = assertIs<AuthTokenResult.Success>(result)
        assertEquals("access", success.accessToken.token)
        assertEquals("refresh", success.refreshToken.token)
    }

    @Test
    fun unparseableSuccessBodyStaysUnknown() = runTest {
        // A 200 we cannot read is genuinely unexpected and should not be mistaken for either case.
        val result = exchangeRefreshToken(
            MockEngine { respondJson(HttpStatusCode.OK, """{"unexpected":true}""") }
        )

        assertIs<AuthTokenResult.Error.UnknownError>(result)
    }

    private suspend fun exchangeRefreshToken(engine: MockEngine): AuthTokenResult =
        NetworkAuthRepository(
            environment = AuthEnvironment.STAGING,
            additionalHttpHeadersProvider = { emptyMap() },
            httpClientEngine = engine,
        ).exchange(RefreshTokenGrant("refresh-token"))

    private fun MockRequestHandleScope.respondJson(status: HttpStatusCode, body: String) =
        respond(
            content = body,
            status = status,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
        )
}
