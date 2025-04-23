package com.example.mixify

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

@Composable
fun HomeScreen() {
    val offsetY = remember { Animatable(300f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(true) {
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 700, easing = EaseOutCubic)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 🌊 Waves in the background
        AnimatedWaveBackground(
            modifier = Modifier
                .fillMaxSize()
        )

        // 🌟 Foreground content with animations
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = offsetY.value
                    this.alpha = alpha.value
                }
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome back, Mixifier 🎧",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Spotify Playlists",
                fontSize = 18.sp,
                color = Color(0xFF1DB954)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(5) { index ->
                    PlaylistCard(title = "Playlist #$index")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Mixify Features",
                fontSize = 18.sp,
                color = Color(0xFFFF4F5A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            FeatureGrid()
        }
    }
}

@Composable
fun AnimatedWaveBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val wave1Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing)
        )
    )

    val wave2Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing)
        )
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        fun createWavePath(offset: Float, amplitude: Float, frequency: Float): Path {
            val path = Path()
            path.moveTo(0f, height)
            for (x in 0..width.toInt()) {
                val y = amplitude * sin((x * frequency + offset)) + height / 2
                path.lineTo(x.toFloat(), y.toFloat())
            }
            path.lineTo(width, height)
            path.close()
            return path
        }

        drawPath(
            path = createWavePath(wave1Offset, 40f, 0.02f),
            color = Color(0xFF1DB954).copy(alpha = 0.2f) // green
        )

        drawPath(
            path = createWavePath(wave2Offset, 30f, 0.025f),
            color = Color.Red.copy(alpha = 0.15f) // red
        )
    }
}

@Composable
fun PlaylistCard(title: String) {
    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .size(150.dp, 100.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(8.dp),
            color = Color.White
        )
    }
}

@Composable
fun FeatureGrid() {
    Column {
        Text(
            text = "• Sticky Notes on Songs 🎵",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "• Spotify + YouTube in one app 🔥",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "• Smart AI Friend & Quiz 🤖",
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
