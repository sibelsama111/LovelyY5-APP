package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.Image
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import android.os.VibrationEffect
import android.os.Vibrator
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.model.ProductItem
import com.sibelsama.lovelyy5.model.ProductReview
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.widget.Toast
import java.io.File
import android.content.Context
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale

@Composable
fun ProductDetailScreen(
    product: Product,
    productItem: ProductItem? = null,
    reviewViewModel: ReviewViewModel,
    onAddToCart: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    var reviews by remember { mutableStateOf(listOf<ProductReview>()) }
    var reviewsLoaded by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(1) }
    var images by remember { mutableStateOf(listOf<Uri>()) }

    LaunchedEffect(product.id) {
        if (!reviewsLoaded) {
            try {
                val currentReviews = reviewViewModel.getReviews(product.id).value
                reviews = currentReviews
                reviewsLoaded = true
                android.util.Log.d("ProductDetailScreen", "Reviews loaded once: ${currentReviews.size}")
            } catch (e: Exception) {
                android.util.Log.e("ProductDetailScreen", "Error loading reviews", e)
                reviews = emptyList()
                reviewsLoaded = true
            }
        }
    }

    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            try {
                val internalDir = context.filesDir
                val imageFile = File(internalDir, "temp_image_${System.currentTimeMillis()}.jpg")
                val outputStream = imageFile.outputStream()
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 85, outputStream)
                outputStream.close()

                val imageUri = Uri.fromFile(imageFile)
                images = images + imageUri
                Toast.makeText(context, "📷 Imagen agregada a la valoración", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error al guardar imagen: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        images = images + uris
        Toast.makeText(context, "🖼️ ${uris.size} imagen(es) agregada(s)", Toast.LENGTH_SHORT).show()
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                cameraLauncher.launch(null)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al abrir la cámara: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val mediaPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            galleryLauncher.launch("image/*")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3F8))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(product.tipo.ifBlank { "Detalle" }, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(product.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 16.dp))
        Text(product.description, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp))
        Text("$${product.price.toInt()} CLP", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        val imgs = productItem?.imagenes ?: emptyList()
        if (imgs.isNotEmpty()) {
            val page = remember { mutableStateOf(0) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .pointerInput(imgs) {
                        detectHorizontalDragGestures(onHorizontalDrag = { change, dragAmount ->
                            if (abs(dragAmount) > 20) {
                                if (dragAmount < 0 && page.value < imgs.lastIndex) page.value = page.value + 1
                                else if (dragAmount > 0 && page.value > 0) page.value = page.value - 1
                                change.consume()
                            }
                        })
                    }
            ) {
                ProductImage(imagePath = imgs[page.value], contentDescription = "${product.name} - ${page.value + 1}", modifier = Modifier.fillMaxSize())
            }

            val thumbsToShow = imgs.drop(1).take(3)
            if (thumbsToShow.isNotEmpty()) {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    thumbsToShow.forEachIndexed { idx, path ->
                        val originalIndex = idx + 1
                        Card(modifier = Modifier.size(72.dp).clickable { page.value = originalIndex }) {
                            ProductImage(imagePath = path, contentDescription = "thumb_$originalIndex", modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)))
                        }
                    }
                }
            }
        } else {
            ProductImage(imagePath = null, contentDescription = product.name, modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                onAddToCart()
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB36AE2))
        ) {
            Text("Añadir al carrito", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Especificaciones técnicas", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 16.dp))
        Text(product.description, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        Text("Deje su valoración", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 16.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        OutlinedTextField(value = comment, onValueChange = { comment = it }, label = { Text("Opinión del producto") }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                        try {
                            cameraLauncher.launch(null)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al abrir la cámara: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                    else -> {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                Text("📷 Cámara", color = Color.White)
            }
            Button(onClick = {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    galleryLauncher.launch("image/*")
                } else {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                            galleryLauncher.launch("image/*")
                        }
                        else -> {
                            mediaPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))) {
                Text("🖼️ Galería", color = Color.White)
            }
        }
        if (images.isNotEmpty()) {
            Text(
                text = "Imágenes seleccionadas (${images.size}):",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp)) {
                images.forEach { uri ->
                    Box(modifier = Modifier.padding(end = 12.dp)) {
                        Card(
                            modifier = Modifier.size(80.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Imagen para valoración",
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        IconButton(
                            onClick = {
                                images = images.filter { it != uri }
                                Toast.makeText(context, "Imagen eliminada", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Cancel,
                                contentDescription = "Eliminar imagen",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (rating > 1) rating-- }) {
                Icon(Icons.Default.Star, contentDescription = "Menos estrella", tint = if (rating > 1) Color(0xFFB36AE2) else Color.Gray)
            }
            Text("$rating", style = MaterialTheme.typography.bodyLarge)
            IconButton(onClick = { if (rating < 5) rating++ }) {
                Icon(Icons.Default.Star, contentDescription = "Más estrella", tint = if (rating < 5) Color(0xFFB36AE2) else Color.Gray)
            }
            Text("Estrellas", style = MaterialTheme.typography.bodyMedium)
        }
        Button(
            onClick = {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))

                if (name.isNotBlank() && comment.isNotBlank()) {
                    val newReview = ProductReview(
                        productId = product.id,
                        name = name,
                        comment = comment,
                        rating = rating,
                        imageUris = images.map { it.toString() }
                    )

                    reviews = reviews + newReview

                    reviewViewModel.saveReview(newReview)

                    name = ""
                    comment = ""
                    rating = 1
                    images = emptyList()

                    Toast.makeText(context, "¡Valoración guardada exitosamente!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Por favor complete el nombre y el comentario", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB36AE2))
        ) {
            Text("Valorar Producto", color = Color.White)
        }
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Valoraciones", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text("(${reviews.size})", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }

        android.util.Log.d("ProductDetailScreen", "Rendering reviews section with ${reviews.size} reviews - Static: $reviewsLoaded")
        Spacer(modifier = Modifier.height(8.dp))

        if (reviews.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Text(
                    text = "Aún no hay valoraciones para este producto.\n¡Sé el primero en valorarlo!",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            reviews.forEachIndexed { index, review ->
                key("review_${review.productId}_${review.name}_${review.comment.hashCode()}_$index") {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(review.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.width(8.dp))
                            repeat(review.rating) {
                                Icon(Icons.Default.Star, contentDescription = "Estrella", tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                            }
                            repeat(5 - review.rating) {
                                Icon(Icons.Default.Star, contentDescription = "Estrella vacía", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(review.comment, style = MaterialTheme.typography.bodySmall)

                        if (review.imageUris.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "📷 ${review.imageUris.size} imagen(es)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                review.imageUris.forEach { uriStr ->
                                    Card(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(end = 8.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(uriStr),
                                            contentDescription = "Imagen de valoración",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
