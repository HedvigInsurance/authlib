package com.hedvig.authlib.url

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class OtpResendUrl(
    @SerialName("resendUrl")
    public val url: String
)