package com.ubb.david.playlistcreator.data

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .also { builder ->
                runBlocking {
                    suspendCoroutine { continuation: Continuation<String?> ->
                        Authenticator.user?.getIdToken(true)?.addOnSuccessListener { tokenResult ->
                            val idToken = tokenResult.token
                            continuation.resume(idToken)
                        }?.addOnFailureListener {
                            continuation.resume(null)
                        }
                    }
                }?.let { accessToken -> builder.addHeader("Authorization", "Bearer $accessToken") }
            }
            .build()
        return chain.proceed(request)
    }
}