package com.hedvig.authlib.connectpayment

import com.hedvig.authlib.AuthEnvironment
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import okhttp3.OkHttpClient

@Suppress("FunctionName")
public fun OkHttpNetworkPaymentRepository(
    environment: AuthEnvironment,
    additionalHttpHeadersProvider: () -> Map<String, String>,
    okHttpClientBuilder: OkHttpClient.Builder,
): PaymentRepository {
    return NetworkPaymentRepository(
        environment = environment,
        additionalHttpHeadersProvider = additionalHttpHeadersProvider,
        httpClientEngine = OkHttpEngine(
            OkHttpConfig().apply { preconfigured = okHttpClientBuilder.build() }
        ),
    )
}