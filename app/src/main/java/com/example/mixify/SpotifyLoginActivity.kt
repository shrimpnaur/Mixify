package com.example.mixify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse

class SpotifyLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = AuthorizationClient.getResponse(RESULT_OK, intent)

        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {
                val accessToken = response.accessToken
                Log.d("SpotifyAuth", "Access Token: $accessToken")

                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.putExtra("ACCESS_TOKEN", accessToken)
                startActivity(mainIntent)
                finish()
            }

            AuthorizationResponse.Type.ERROR -> {
                Log.e("SpotifyAuth", "Error: ${response.error}")
            }

            else -> {
                Log.e("SpotifyAuth", "Auth cancelled or unknown")
            }
        }
    }
}
