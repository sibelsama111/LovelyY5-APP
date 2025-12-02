package com.sibelsama.lovelyy5.ui.dopamina

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sibelsama.lovelyy5.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import com.sibelsama.lovelyy5.R

@Serializable
data class CatImage(val url: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DopaminaScreen(onBackClick: () -> Unit) {
    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true })
            }
        }
    }
    var imageUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (true) {
                isLoading = true
                errorMessage = null
                try {
                    val images: List<CatImage> = client.get("https://api.thecatapi.com/v1/images/search") {
                        header("x-api-key", BuildConfig.CAT_API_KEY)
                    }.body()
                    if (images.isNotEmpty()) {
                        imageUrl = images[0].url
                    } else {
                        errorMessage = "No se encontraron imágenes de gatos."
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorMessage = "Error al cargar la imagen: ${e.message}"
                } finally {
                    isLoading = false
                }
                delay(10000)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dopamina Felina") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "En Lovely Y5 nos importan nuestros clientes <3",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "La salud mental es el pilar del bienestar. Influye en cómo pensamos, sentimos y actuamos, permitiéndonos afrontar el estrés, tener relaciones sanas y alcanzar nuestro potencial. En un mundo acelerado, cuidar la mente es crucial.\n\nLa compañía de los gatos tiene efectos terapéuticos. Su interacción libera dopamina, asociada al placer. Acariciarlos o su ronroneo reduce el estrés y aumenta la felicidad, generando calma. Para la ansiedad o depresión, son un ancla emocional. \n\nPor eso dejamos estas imágenes de gatos para que tengas un momento de diversión y relajo <3",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                } else if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .placeholder(R.drawable.ic_launcher_foreground) // Reemplaza con un placeholder adecuado
                            .error(R.drawable.ic_launcher_background) // Reemplaza con una imagen de error adecuada
                            .build(),
                        contentDescription = "Gato Aleatorio",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
