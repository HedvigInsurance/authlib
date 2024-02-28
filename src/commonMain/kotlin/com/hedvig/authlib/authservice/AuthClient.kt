package com.hedvig.authlib.authservice

import com.hedvig.authlib.AuthEnvironment
import com.hedvig.authlib.authservice.otplogin.LoginOtpInput
import com.hedvig.authlib.authservice.otplogin.LoginOtpResponse
import com.hedvig.authlib.authservice.otpswedenlogin.LoginOtpSwedenInput
import com.hedvig.authlib.authservice.otpswedenlogin.LoginOtpSwedenResponse
import com.hedvig.authlib.authservice.swedenlogin.LoginSwedenInput
import com.hedvig.authlib.authservice.swedenlogin.LoginSwedenResponse
import com.hedvig.authlib.baseUrl
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Service mapping the API of `HedvigInsurance/auth`
 */
internal class AuthService(
    private val environment: AuthEnvironment,
    private val ktorClient: HttpClient,
) {
    @Throws(NoTransformationFoundException::class, Throwable::class)
    suspend fun memberLoginSweden(
        personalNumber: String?,
    ): LoginSwedenResponse {
        val response = ktorClient.post("${environment.baseUrl}/member-login") {
            headers["hedvig-bankid-v6"] = "true"
            contentType(ContentType.Application.Json)
            setBody(LoginSwedenInput(personalNumber))
        }
        return response.body<LoginSwedenResponse>()
    }

    @Throws(NoTransformationFoundException::class, Throwable::class)
    suspend fun memberLoginOtp(
        otpLoginCountry: LoginOtpInput.OtpLoginCountry,
        personalNumber: String
    ): LoginOtpResponse {
        val response = ktorClient.post("${environment.baseUrl}/member-login") {
            contentType(ContentType.Application.Json)
            setBody(LoginOtpInput(otpLoginCountry, personalNumber))
        }
        return response.body<LoginOtpResponse>()
    }

    /**
     * Used for Qasa login attempts
     */
    @Throws(NoTransformationFoundException::class, Throwable::class)
    suspend fun memberLoginOtpSweden(
        email: String,
    ): LoginOtpSwedenResponse {
        val response = ktorClient.post("${environment.baseUrl}/member-login") {
            contentType(ContentType.Application.Json)
            setBody(LoginOtpSwedenInput(email))
        }
        return response.body<LoginOtpSwedenResponse>()
    }
}
