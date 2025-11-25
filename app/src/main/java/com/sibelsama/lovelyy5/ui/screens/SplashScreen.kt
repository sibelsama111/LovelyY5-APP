package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SplashScreen(onFinished: () -> Unit = {}) {
    val alpha = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(5000L)
        alpha.animateTo(0f, animationSpec = tween(durationMillis = 600))
        onFinished()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().alpha(alpha.value)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(shape = CircleShape, modifier = Modifier.size(140.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                    AsyncImage(model = "file:///android_asset/images/app_icon.png", contentDescription = "Logo", modifier = Modifier.padding(24.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Lovely Y5", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}
