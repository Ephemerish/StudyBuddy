package com.example.studybuddy.presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.studybuddy.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient (
    private val context: Context,
    private val onTapClient: SignInClient
){
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            onTapClient.beginSignIn(
                builtSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signout( ){
        try {
            onTapClient.signOut().await()
            auth.signOut()
        }catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedUser(): UserData? = auth.currentUser?.run{
        UserData(
            userId = uid,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString(),
            userEmail = email
        )
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = onTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user

            // Check if the user's email matches the school domain
            if (user?.email?.endsWith("@bisu.edu.ph") == true) {
                SignInResult(
                    data = user.run {
                        UserData(
                            userId = uid,
                            userEmail = email,
                            userName = displayName,
                            profilePictureUrl = photoUrl?.toString()
                        )
                    },
                    errorMessage = null
                )
            } else {
                // If the email doesn't match, sign the user out and provide an error message
                signout()
                SignInResult(
                    data = null,
                    errorMessage = "Invalid email domain. Please use BISU email."
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    private fun builtSignInRequest(): BeginSignInRequest{
        return  BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}