package com.example.mixify

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class SpotifyLoginActivity : AppCompatActivity() {

    private val clientId = "b34fda919b424aea8ced493553526602"
    private val redirectUri = "mixify://callback"
    private val requestCode = 1337

    companion object {
        private var isAuthInProgress = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentData = intent?.data
        Log.d("SpotifyAuth", "onCreate, intent data: $intentData")

        // Check if handling callback from Spotify
        if (intentData?.scheme == "mixify") {
            Log.d("SpotifyAuth", "Handling callback: $intentData")
            handleAuthResponse()
            return
        }

        if (!isAuthInProgress) {
            isAuthInProgress = true

            val request = AuthorizationRequest.Builder(
                clientId,
                AuthorizationResponse.Type.TOKEN,
                redirectUri
            )
                .setScopes(arrayOf("user-read-private", "user-read-email"))
                .setShowDialog(true)
                .build()

            val loginIntent = AuthorizationClient.createLoginActivityIntent(this, request)
            Log.d("SpotifyAuth", "Starting login activity")
            startActivityForResult(loginIntent, requestCode)
        }
    }

    private fun handleAuthResponse() {
        Log.d("SpotifyAuth", "Inside handleAuthResponse")

        val uri = intent?.data
        val fragment = uri?.fragment
        Log.d("SpotifyAuth", "Callback URI fragment: $fragment")

        if (fragment != null && fragment.contains("access_token")) {
            val accessToken = fragment.substringAfter("access_token=").substringBefore("&")

            Log.d("SpotifyAuth", "Token from callback: $accessToken")

            val resultIntent = Intent()
            resultIntent.putExtra("ACCESS_TOKEN", accessToken)
            setResult(RESULT_OK, resultIntent)
        } else {
            Log.e("SpotifyAuth", "No token found in callback")
            setResult(RESULT_CANCELED)
        }

        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == this.requestCode) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            Log.d("SpotifyAuth", "Auth response type: ${response.type}")

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val accessToken = response.accessToken
                    Log.d("SpotifyAuth", "Access Token: $accessToken")

                    val resultIntent = Intent()
                    resultIntent.putExtra("ACCESS_TOKEN", accessToken)
                    setResult(RESULT_OK, resultIntent)
                }

                AuthorizationResponse.Type.ERROR -> {
                    Log.e("SpotifyAuth", "Auth error: ${response.error}")
                    setResult(RESULT_CANCELED)
                }

                else -> {
                    Log.e("SpotifyAuth", "Auth cancelled or failed")
                    setResult(RESULT_CANCELED)
                }
            }

            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isAuthInProgress = false
    }
}
