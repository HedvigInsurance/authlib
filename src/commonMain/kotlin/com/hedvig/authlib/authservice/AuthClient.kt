package com.hedvig.authlib.authservice

import com.hedvig.authlib.AuthEnvironment
import com.hedvig.authlib.StatusUrl
import com.hedvig.authlib.authservice.model.otp.login.LoginOtpInput
import com.hedvig.authlib.authservice.model.otp.login.LoginOtpResponse
import com.hedvig.authlib.authservice.model.otp.swedenlogin.LoginOtpSwedenInput
import com.hedvig.authlib.authservice.model.otp.swedenlogin.LoginOtpSwedenResponse
import com.hedvig.authlib.authservice.model.otp.verify.OtpVerifyInput
import com.hedvig.authlib.authservice.model.otp.verify.OtpVerifyResponse
import com.hedvig.authlib.authservice.swedenlogin.LoginSwedenInput
import com.hedvig.authlib.authservice.swedenlogin.LoginSwedenResponse
import com.hedvig.authlib.baseUrl
import com.hedvig.authlib.authservice.model.swedenstatus.LoginStatusResponse
import com.hedvig.authlib.url.OtpResendUrl
import com.hedvig.authlib.url.OtpVerifyUrl
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

    @Throws(NoTransformationFoundException::class, Throwable::class)
    suspend fun loginStatus(statusUrl: StatusUrl): LoginStatusResponse {
        val response = ktorClient.get("${environment.baseUrl}${statusUrl.url}")
        return response.body<LoginStatusResponse>()
    }

    @Throws(NoTransformationFoundException::class, Throwable::class)
    suspend fun otpVerify(otp: String, otpVerifyUrl: OtpVerifyUrl): OtpVerifyResponse {
        val response = ktorClient.post("${environment.baseUrl}${otpVerifyUrl.url}") {
            contentType(ContentType.Application.Json)
            setBody(OtpVerifyInput(otp))
        }
        return response.body<OtpVerifyResponse>()
    }

    /**
     * @return [true] if the request had a status 200 as a response
     */
    @Throws(Throwable::class)
    suspend fun otpResend(otpResendUrl: OtpResendUrl): Boolean {
        val response = ktorClient.post("${environment.baseUrl}${otpResendUrl.url}")
        return response.status == HttpStatusCode.OK
    }
}




