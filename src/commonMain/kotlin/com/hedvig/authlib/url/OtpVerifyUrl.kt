package com.hedvig.authlib.url

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class OtpVerifyUrl(
    @SerialName("verifyUrl")
    public val url: String
)
