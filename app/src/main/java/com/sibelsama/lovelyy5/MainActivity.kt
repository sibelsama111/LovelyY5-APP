package com.sibelsama.lovelyy5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sibelsama.lovelyy5.ui.screens.NavGraph
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LovelyY5APPTheme {
                var showSplash by remember { mutableStateOf(true) }
                // Mostrar splash por 5 segundos
                LaunchedEffect(Unit) {
                    delay(5000L)
                    showSplash = false
                }

                if (showSplash) {
                    com.sibelsama.lovelyy5.ui.screens.SplashScreen()
                } else {
                    NavGraph()
                }
            }
        }
    }
}
