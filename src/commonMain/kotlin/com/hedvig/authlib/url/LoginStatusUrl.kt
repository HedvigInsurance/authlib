package com.hedvig.authlib.url

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class LoginStatusUrl(public val url: String)
