package com.sibelsama.lovelyy5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.data.DataManager
import com.sibelsama.lovelyy5.ui.screens.NavGraph
import com.sibelsama.lovelyy5.ui.screens.PermissionScreen
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Ups! Algo salió mal",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Intentar de nuevo")
                }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            enableEdgeToEdge()
            setContent {
                LovelyY5APPTheme {
                    MainContent()
                }
            }
        } catch (e: Exception) {
            // Si hay un error crítico, cerrar la app graciosamente
            e.printStackTrace()
            finish()
        }
    }
}

@Composable
private fun MainContent() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val dataManager = remember { DataManager(context) }
    val isFirstTime by dataManager.isFirstTime.collectAsState(initial = true)
    var showPermissionScreen by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(isFirstTime) {
        showPermissionScreen = isFirstTime
    }

    when {
        showError -> {
            ErrorScreen(
                message = errorMessage,
                onRetry = {
                    showError = false
                    showPermissionScreen = true
                }
            )
        }
        showPermissionScreen -> {
            PermissionScreen(
                onPermissionsGranted = {
                    showPermissionScreen = false
                }
            )
        }
        else -> {
            NavGraph()
        }
    }
}
