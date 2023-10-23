package com.hedvig.authlib

import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import okhttp3.OkHttpClient

fun OkHttpNetworkAuthRepository(
  environment: AuthEnvironment,
  additionalHttpHeaders: Map<String, String>,
  callbacks: Callbacks,
  okHttpClientBuilder: OkHttpClient.Builder.() -> Unit,
): AuthRepository {
  return NetworkAuthRepository(
    environment = environment,
    additionalHttpHeaders = additionalHttpHeaders,
    callbacks = callbacks,
    httpClientEngine = OkHttpEngine(OkHttpConfig().apply { config(okHttpClientBuilder) }),
  )
}