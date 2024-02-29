package com.hedvig.authlib.authservice.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LoginStatusResponse(
    val status: LoginStatus,
    val statusText: String,
    val authorizationCode: String?,
    val seBankIdProperties: BankIdProperties?
) {

    @Serializable
    data class BankIdProperties(
        val liveQrCodeData: String,
        @SerialName("bankidAppOpened")
        val bankIdAppOpened: Boolean,
    )

    enum class LoginStatus {
        PENDING, FAILED, COMPLETED
    }
}

//private fun LoginStatusResponse.toLoginStatusResult(): LoginStatusResult = when (status) {
//    LoginStatusResponse.LoginStatus.PENDING -> LoginStatusResult.Pending(statusText, seBankIdProperties?.liveQrCodeData)
//    LoginStatusResponse.LoginStatus.FAILED -> LoginStatusResult.Failed(statusText)
//    LoginStatusResponse.LoginStatus.COMPLETED -> {
//        require(authorizationCode != null) {
//            "Login status completed but did not receive authorization code"
//        }
//
//        val code = AuthorizationCodeGrant(authorizationCode)
//        LoginStatusResult.Completed(code)
//    }
//}