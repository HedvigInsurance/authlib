package com.hedvig.authlib.url

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
internal value class LoginStatusUrl(internal val url: String)
