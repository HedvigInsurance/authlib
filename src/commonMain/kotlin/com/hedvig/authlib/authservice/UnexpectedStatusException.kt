package com.hedvig.authlib.authservice

import io.ktor.http.*

/**
 * Thrown when an endpoint answers with a status whose body is not the success payload.
 *
 * Without it the error body is handed to the success deserializer, which fails on the missing
 * fields, so a rejected token and a server outage both arrive as the same unparseable response and
 * the caller has no way to tell them apart.
 */
internal class UnexpectedStatusException(
    val status: HttpStatusCode,
    path: String,
) : Exception("$path responded with $status")
