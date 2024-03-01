package com.hedvig.authlib.connectpayment

public interface PaymentRepository {
    /**
     * Returns the member's authorization code which can be used to start a connect-payment process
     * for NO and DK
     *
     * [webLocale] The locale string that the web uses to represent each locale.
     *             Input may look like "se", "se-en", "no-en" etc.
     */
    public suspend fun getMemberAuthorizationCode(webLocale: String): MemberAuthorizationCodeResult
}
