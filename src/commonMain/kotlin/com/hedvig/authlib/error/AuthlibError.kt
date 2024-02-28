package com.hedvig.authlib.error

public sealed interface AuthlibError {
    public data class Localised(val reason: String) : AuthlibError
    public data class BackendErrorResponse(val message: String, val httpStatusValue: Int) : AuthlibError
    public data class IOError(val message: String) : AuthlibError
    public data class UnknownError(val message: String) : AuthlibError
}