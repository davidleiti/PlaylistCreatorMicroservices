package com.ubb.david.auth

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.Authentication
import io.ktor.auth.AuthenticationPipeline
import io.ktor.auth.AuthenticationProvider
import io.ktor.auth.Principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.ApplicationRequest
import io.ktor.request.authorization
import io.ktor.response.respond
import org.slf4j.LoggerFactory

object FirebaseAuthenticationProvider : AuthenticationProvider(ProviderConfig()) {

    internal val token: (ApplicationCall) -> String? = { call -> call.request.parseAuthorizationToken() }
    internal val principal: ((userId: String) -> Principal?) = { userId -> User(userId) }

    private const val NAME = "FirebaseAuthProvider"

    private class ProviderConfig : AuthenticationProvider.Configuration(NAME)
}

fun Authentication.Configuration.firebase() {
    val logger = LoggerFactory.getLogger("FirebaseAuth")
    val provider = FirebaseAuthenticationProvider
    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        try {
            val token = provider.token(call) ?: throw idTokenNotFoundException
            val uid = FirebaseAuth.getInstance().verifyIdToken(token).uid
            provider.principal(uid)?.let { principal -> context.principal(principal) }
        } catch (cause: Throwable) {
            val message = if (cause is FirebaseAuthException) {
                "Authentication failed: ${cause.message ?: cause.javaClass.simpleName}"
            } else {
                cause.message ?: cause.javaClass.simpleName
            }
            logger.trace(message)
            call.respond(HttpStatusCode.Unauthorized, message)
            context.challenge.complete()
            finish()
        }
    }
    register(provider)
}

val idTokenNotFoundException: FirebaseAuthException
    get() = FirebaseAuthException(FirebaseException(ErrorCode.UNAUTHENTICATED, "No token could be found", null))

fun ApplicationRequest.parseAuthorizationToken(): String? =
        authorization()?.let { authValue -> authValue.split(" ")[1] }