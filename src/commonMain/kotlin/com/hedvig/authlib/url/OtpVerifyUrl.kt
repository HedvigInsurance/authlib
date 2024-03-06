package com.hedvig.authlib.url

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
internal value class OtpVerifyUrl(
    @SerialName("verifyUrl")
    internal val url: String
)
