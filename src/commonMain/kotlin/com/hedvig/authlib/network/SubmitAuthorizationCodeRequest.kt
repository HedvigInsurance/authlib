package com.hedvig.authlib.network

import com.hedvig.authlib.AuthorizationCode
import com.hedvig.authlib.LoginAuthorizationCode
import com.hedvig.authlib.RefreshCode

// TODO

//fun AuthorizationCode.createRequestBody(): FormBody {
//
//  val builder = FormBody.Builder()
//
//  when (this) {
//    is LoginAuthorizationCode -> {
//      builder.add("authorizationCode", code)
//      builder.add("grant_type", "authorization_code")
//    }
//    is RefreshCode -> {
//      builder.add("refresh_token", code)
//      builder.add("grant_type", "refresh_token")
//    }
//  }
//
//  return builder.build()
//}
