package com.sibelsama.lovelyy5.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.sibelsama.lovelyy5.R
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.model.ProductReview
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel

@Composable
fun ProductDetailScreen(
    product: Product,
    reviewViewModel: ReviewViewModel,
    onAddToCart: () -> Unit,
    onBack: () -> Unit = {}
) {
    val reviews by reviewViewModel.getReviews(product.id).collectAsState()
    var name by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(1) }
    var images by remember { mutableStateOf(listOf<Uri>()) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    // Launcher para tomar foto con cámara - simplificado para evitar crashes
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        try {
            bitmap?.let {
                // Por ahora solo mostramos mensaje, evitamos crash por conversión URI
                errorMessage = "Foto capturada correctamente (función en desarrollo)"
                showError = true
            }
        } catch (e: Exception) {
            errorMessage = "Error al capturar la foto: ${e.message}"
            showError = true
        }
    }

    // Launcher para seleccionar imágenes de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        try {
            if (uris.isNotEmpty()) {
                // Limitamos a máximo 5 imágenes para evitar problemas de memoria
                val newImages = (images + uris).take(5)
                images = newImages
            }
        } catch (e: Exception) {
            errorMessage = "Error al seleccionar imágenes: ${e.message}"
            showError = true
        }
    }

    val haptic = LocalHapticFeedback.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            // Header con botón de retroceso
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = product.category.ifBlank { "Productos" },
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Imagen principal con mejor manejo de errores
            val mainImagePainter = if (product.images.isNotEmpty()) {
                rememberAsyncImagePainter(
                    model = product.images[0],
                    onError = {
                        // En caso de error al cargar imagen, usar placeholder
                    }
                )
            } else {
                painterResource(id = R.drawable.ic_launcher_background)
            }

            Image(
                painter = mainImagePainter,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Información del producto
            Text(
                product.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                product.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                "$${product.price.toInt()} CLP",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Categoría: ${product.category}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                "Stock: ${product.stock}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (product.specs.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Especificaciones:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    product.specs,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Thumbnails de imágenes adicionales
            if (product.images.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    product.images.drop(1).forEach { url ->
                        Image(
                            painter = rememberAsyncImagePainter(url),
                            contentDescription = "Foto producto",
                            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón añadir al carrito
            Button(
                onClick = {
                    try {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAddToCart()
                        errorMessage = "Producto añadido al carrito"
                        showError = true
                    } catch (e: Exception) {
                        errorMessage = "Error al añadir al carrito: ${e.message}"
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text("Añadir al carrito", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            // Sección de crear valoración
            Text(
                "Deja tu valoración",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { newValue ->
                    // Sanitizar entrada limitando caracteres especiales
                    name = newValue.take(100).filter { it.isLetterOrDigit() || it.isWhitespace() || it in "áéíóúñÁÉÍÓÚÑ" }
                },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                isError = name.trim().isNotBlank() && name.trim().length < 2,
                supportingText = if (name.trim().isNotBlank() && name.trim().length < 2) {
                    { Text("El nombre debe tener al menos 2 caracteres") }
                } else null
            )

            OutlinedTextField(
                value = comment,
                onValueChange = { newValue ->
                    // Limitar longitud del comentario
                    comment = newValue.take(500)
                },
                label = { Text("Opinión del producto") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                minLines = 3,
                maxLines = 8,
                isError = comment.trim().isNotBlank() && comment.trim().length < 10,
                supportingText = if (comment.trim().isNotBlank() && comment.trim().length < 10) {
                    { Text("El comentario debe tener al menos 10 caracteres") }
                } else {
                    { Text("${comment.length}/500 caracteres") }
                }
            )

            // Botones de cámara y galería con validaciones
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        try {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (images.size < 5) {
                                cameraLauncher.launch(null)
                            } else {
                                errorMessage = "Máximo 5 imágenes permitidas"
                                showError = true
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error al abrir cámara: ${e.message}"
                            showError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    enabled = images.size < 5
                ) {
                    Text("Cámara", color = Color.White)
                }

                Button(
                    onClick = {
                        try {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (images.size < 5) {
                                galleryLauncher.launch("image/*")
                            } else {
                                errorMessage = "Máximo 5 imágenes permitidas"
                                showError = true
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error al abrir galería: ${e.message}"
                            showError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    enabled = images.size < 5
                ) {
                    Text("Galería (${images.size}/5)", color = Color.White)
                }
            }

            // Mostrar imágenes seleccionadas con mejor manejo de errores
            if (images.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    images.forEachIndexed { index, uri ->
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = uri,
                                    onError = {
                                        // Manejo de error al cargar imagen
                                    }
                                ),
                                contentDescription = "Imagen valoración $index",
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            IconButton(
                                onClick = {
                                    try {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        images = images.filter { it != uri }
                                } catch (e: Exception) {
                                    errorMessage = "Error al eliminar imagen: ${e.message}"
                                    showError = true
                                }
                                },
                                modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar imagen",
                                    tint = Color.Red,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Selector de rating
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Calificación: ", style = MaterialTheme.typography.bodyMedium)
                Row {
                    repeat(5) { index ->
                        IconButton(
                            onClick = { rating = index + 1 }
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Estrella ${index + 1}",
                                tint = if (index < rating) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
                    }
                }
                Text("($rating/5)", style = MaterialTheme.typography.bodySmall)
            }

            // Botón de valorar con validaciones mejoradas
            Button(
                onClick = {
                    try {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                        // Validaciones
                        when {
                            name.isBlank() -> {
                                errorMessage = "Por favor ingresa tu nombre"
                                showError = true
                            }
                            name.length < 2 -> {
                                errorMessage = "El nombre debe tener al menos 2 caracteres"
                                showError = true
                            }
                            comment.isBlank() -> {
                                errorMessage = "Por favor ingresa tu comentario"
                                showError = true
                            }
                            comment.length < 10 -> {
                                errorMessage = "El comentario debe tener al menos 10 caracteres"
                                showError = true
                            }
                            isSubmitting -> {
                                errorMessage = "Ya se está enviando la valoración, espera un momento"
                                showError = true
                            }
                            else -> {
                                isSubmitting = true
                                try {
                                    reviewViewModel.saveReview(
                                        ProductReview(
                                            productId = product.id,
                                            name = name.trim(),
                                            comment = comment.trim(),
                                            rating = rating,
                                            imageUris = images.map { it.toString() }
                                        )
                                    )
                                    // Limpiar formulario tras éxito
                                    name = ""
                                    comment = ""
                                    rating = 1
                                    images = emptyList()
                                    errorMessage = "¡Valoración enviada exitosamente!"
                                    showError = true
                                } catch (e: Exception) {
                                    errorMessage = "Error al enviar valoración: ${e.message}"
                                    showError = true
                                } finally {
                                    isSubmitting = false
                                }
                            }
                        }
                    } catch (e: Exception) {
                        errorMessage = "Error inesperado: ${e.message}"
                        showError = true
                        isSubmitting = false
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviando...", color = Color.White)
                } else {
                    Text("Valorar", color = Color.White)
                }
            }

            // Sección de valoraciones existentes
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Valoraciones (${reviews.size})",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (reviews.isEmpty()) {
                Text(
                    "Aún no hay valoraciones para este producto. ¡Sé el primero en valorarlo!",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }
        }

        // Mostrar valoraciones como items separados para mejor rendimiento
        items(reviews.size) { index ->
            val review = reviews[index]
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Header con nombre y estrellas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            review.name.take(50), // Limitar longitud del nombre
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row {
                            repeat(5) { starIndex ->
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = if (starIndex < review.rating) "Estrella llena" else "Estrella vacía",
                                    tint = if (starIndex < review.rating) MaterialTheme.colorScheme.primary else Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Comentario
                    Text(
                        review.comment,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Imágenes de la valoración si existen
                    if (review.imageUris.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            review.imageUris.take(5).forEachIndexed { imgIndex, uriStr ->
                                // Validar URI antes de usar - alternativa compatible
                                val isValidUri = try {
                                    uriStr.toUri()
                                    true
                                } catch (_: Exception) {
                                    false
                                }

                                if (isValidUri) {
                                    val uri = uriStr.toUri()
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = uri,
                                            onError = {
                                                // Manejo de error al cargar imagen de valoración
                                            }
                                        ),
                                        contentDescription = "Imagen valoración $imgIndex",
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .padding(end = 8.dp)
                                    )
                                } else {
                                    // Fallback para URIs inválidos
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Gray)
                                            .padding(end = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Error", fontSize = 10.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Mensaje de estado/error
        item {
            if (showError && errorMessage.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (errorMessage.contains("exitosamente") || errorMessage.contains("carrito")) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        }
                    )
                ) {
                    Text(
                        text = errorMessage,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                LaunchedEffect(errorMessage) {
                    kotlinx.coroutines.delay(3000)
                    showError = false
                    errorMessage = ""
                }
            }
        }
    }
}
