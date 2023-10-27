package com.hedvig.authlib.connectpayment

import com.hedvig.authlib.AuthEnvironment
import com.hedvig.authlib.MemberAuthorizationCodeResult
import com.hedvig.authlib.MemberPaymentUrl
import com.hedvig.authlib.baseUrl
import com.hedvig.authlib.internal.commonKtorConfiguration
import com.hedvig.authlib.network.MemberAuthorizationCodesResponse
import com.hedvig.authlib.webBaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.post
import kotlinx.coroutines.CancellationException

public class NetworkPaymentRepository(
    private val environment: AuthEnvironment,
    private val additionalHttpHeadersProvider: () -> Map<String, String>,
    private val httpClientEngine: HttpClientEngine? = null,
) : PaymentRepository {
    private val ktorClient: HttpClient = run {
        val httpClientConfig: HttpClientConfig<*>.() -> Unit = {
            commonKtorConfiguration(additionalHttpHeadersProvider).invoke(this)
        }
        if (httpClientEngine == null) {
            HttpClient {
                httpClientConfig()
            }
        } else {
            HttpClient(httpClientEngine) {
                httpClientConfig()
            }
        }
    }

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
