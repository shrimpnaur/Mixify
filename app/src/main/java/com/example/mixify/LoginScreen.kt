package com.example.mixify

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    // Set up launcher for Spotify auth activity
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isLoading = false

        if (result.resultCode == Activity.RESULT_OK) {
            val token = result.data?.getStringExtra("ACCESS_TOKEN")
            if (token != null) {
                // Store token in SharedPreferences
                val sharedPrefs = context.getSharedPreferences("mixify_prefs", Activity.MODE_PRIVATE)
                sharedPrefs.edit().putString("spotify_token", token).apply()

                // Navigate to home screen
                navController.navigate(Screen.Home.route) {
                    // Clear the back stack so the user can't go back to the login screen
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Mixify")

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading = true
                    val intent = Intent(context, SpotifyLoginActivity::class.java)
                    launcher.launch(intent)
                }
            ) {
                Text("Login with Spotify")
            }
        }
    }
}
