package com.hedvig.authlib.connectpayment

import com.hedvig.authlib.AuthEnvironment
import com.hedvig.authlib.baseUrl
import com.hedvig.authlib.internal.buildKtorClient
import com.hedvig.authlib.webBaseUrl
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import kotlinx.coroutines.CancellationException
import kotlin.jvm.JvmInline

public class NetworkPaymentRepository(
    private val environment: AuthEnvironment,
    additionalHttpHeadersProvider: () -> Map<String, String>,
    httpClientEngine: HttpClientEngine? = null,
) : PaymentRepository {
    private val ktorClient: HttpClient = buildKtorClient(httpClientEngine, additionalHttpHeadersProvider)

    override suspend fun getMemberAuthorizationCode(
        webLocale: String,
    ): MemberAuthorizationCodeResult {
        return try {
            val authorizationCode = ktorClient
                .post("${environment.baseUrl}/member-authorization-codes")
                .body<MemberAuthorizationCodesResponse>()
                .authorizationCode
            val memberPaymentUrl = MemberPaymentUrl(
                url = buildString {
                    append(environment.webBaseUrl)
                    append("/")
                    append(webLocale)
                    append("/payment/connect-legacy/start?authorizationCode=")
                    append(authorizationCode)
                }
            )
            MemberAuthorizationCodeResult.Success(memberPaymentUrl)
        } catch (e: Throwable) {
            if (e is CancellationException) throw e
            MemberAuthorizationCodeResult.Error(e)
        }
    }
}

public sealed interface MemberAuthorizationCodeResult {
    public data class Error(val error: Throwable) : MemberAuthorizationCodeResult

    public data class Success(
        public val memberPaymentUrl: MemberPaymentUrl,
    ) : MemberAuthorizationCodeResult
}

@JvmInline
public value class MemberPaymentUrl(
    public val url: String
)
