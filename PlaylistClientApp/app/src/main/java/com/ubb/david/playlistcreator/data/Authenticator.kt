package com.ubb.david.playlistcreator.data

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ubb.david.playlistcreator.R

object Authenticator {

    private const val DEFAULT_REQUEST_CODE = 123

    private var firebaseAuth = FirebaseAuth.getInstance()

    val user: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun authenticate(fragment: Fragment) {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        fragment.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.ic_spotify_logo)
                .build(),
            DEFAULT_REQUEST_CODE,
            null
        )
    }

    fun logout() = firebaseAuth.signOut()

    fun handleAuthenticationResult(requestCode: Int, resultCode: Int, data: Intent?): AuthenticationResult? {
        return if (requestCode == DEFAULT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                AuthenticationResult.Success
            } else {
                val error = IdpResponse.fromResultIntent(data)?.error
                if (error == null) {
                    AuthenticationResult.Failure.UserCancelled
                } else {
                    AuthenticationResult.Failure.Error(error.errorCode, error.message ?: "No error message.")
                }
            }
        } else {
            null
        }
    }

}

sealed class AuthenticationResult {
    object Success : AuthenticationResult()
    sealed class Failure : AuthenticationResult() {
        object UserCancelled : Failure()
        class Error(val errorCode: Int, val errorMessage: String) : Failure()
    }
}