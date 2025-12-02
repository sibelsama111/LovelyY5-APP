package com.sibelsama.lovelyy5.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.model.ProductItem
import com.sibelsama.lovelyy5.model.ProductReview
import com.sibelsama.lovelyy5.ui.components.ProductImage
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel
import java.io.File
import kotlin.math.abs

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
    val newReviewImages by reviewViewModel.newReviewImages.collectAsState()
    var showImageViewer by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(product.id) {
        if (!reviewsLoaded) {
            try {
                val currentReviews = reviewViewModel.getReviews(product.id).value
                reviews = currentReviews
                reviewsLoaded = true
            } catch (e: Exception) {
                android.util.Log.e("ProductDetailScreen", "Error loading reviews", e)
                reviews = emptyList()
                reviewsLoaded = true
            }
        }
    }

    if (showImageViewer && selectedImageUri != null) {
        Dialog(onDismissRequest = { showImageViewer = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable { showImageViewer = false },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Visor de imagen",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            try {
                val internalDir = context.filesDir
                val imageFile = File(internalDir, "temp_image_${System.currentTimeMillis()}.jpg")
                imageFile.outputStream().use { out ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 85, out)
                }
                val imageUri = Uri.fromFile(imageFile)
                reviewViewModel.addImageToNewReview(imageUri.toString())
                Toast.makeText(context, "📷 Imagen agregada a la valoración", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error al guardar imagen: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        uris.forEach { reviewViewModel.addImageToNewReview(it.toString()) }
        if (uris.isNotEmpty()) {
            Toast.makeText(context, "🖼️ ${uris.size} imagen(es) agregada(s)", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val mediaPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            // Still launch, system might handle it with a picker without the permission
            galleryLauncher.launch("image/*")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (abs(dragAmount) > 20) {
                                if (dragAmount < 0 && page.value < imgs.lastIndex) page.value++
                                else if (dragAmount > 0 && page.value > 0) page.value--
                                change.consume()
                            }
                        }
                    }
            ) {
                ProductImage(imagePath = imgs[page.value], contentDescription = "${product.name} - ${page.value + 1}", modifier = Modifier.fillMaxSize())
            }
        } else {
            ProductImage(imagePath = null, contentDescription = product.name, modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)))
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                onAddToCart()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Añadir al carrito", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Especificaciones técnicas", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 16.dp))
        Text(product.description, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        // Review Form
        Text("Deje su valoración", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 16.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp))
        OutlinedTextField(value = comment, onValueChange = { comment = it }, label = { Text("Opinión del producto") }, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(null)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Text("📷 Cámara", color = Color.White)
            }
            Button(onClick = {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    galleryLauncher.launch("image/*")
                } else {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        galleryLauncher.launch("image/*")
                    } else {
                        mediaPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                Text("🖼️ Galería", color = MaterialTheme.colorScheme.onSecondary)
            }
            Button(onClick = { reviewViewModel.fetchRandomCatImage() }) {
                Text("🐱 Gato")
            }
        }

        if (newReviewImages.isNotEmpty()) {
            Text(
                text = "Imágenes seleccionadas (${newReviewImages.size}):",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                newReviewImages.forEach { uri ->
                    Box(modifier = Modifier.padding(end = 12.dp)) {
                        Card(
                            modifier = Modifier
                                .size(80.dp)
                                .clickable {
                                    selectedImageUri = uri
                                    showImageViewer = true
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "Imagen para valoración",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        IconButton(
                            onClick = {
                                reviewViewModel.removeImageFromNewReview(uri)
                                Toast.makeText(context, "Imagen eliminada", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
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

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { if (rating > 1) rating-- }) {
                Icon(Icons.Default.Star, contentDescription = "Menos estrella", tint = if (rating > 1) MaterialTheme.colorScheme.primary else Color.Gray)
            }
            Text("$rating", style = MaterialTheme.typography.bodyLarge)
            IconButton(onClick = { if (rating < 5) rating++ }) {
                Icon(Icons.Default.Star, contentDescription = "Más estrella", tint = if (rating < 5) MaterialTheme.colorScheme.primary else Color.Gray)
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
                        imageUris = newReviewImages
                    )
                    reviews = reviews + newReview
                    reviewViewModel.saveReview(newReview)
                    reviewViewModel.clearNewReviewImages()

                    name = ""
                    comment = ""
                    rating = 1
                    Toast.makeText(context, "¡Valoración guardada exitosamente!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Por favor complete el nombre y el comentario", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Valorar Producto", color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Reviews List
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Valoraciones", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text("(${reviews.size})", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (reviews.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
            reviews.forEach { review ->
                ReviewItem(review = review) { uri ->
                    selectedImageUri = uri
                    showImageViewer = true
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ReviewItem(review: ProductReview, onImageClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(review.name, fontWeight = FontWeight.Bold)
            Text(review.comment)
            Row {
                (1..5).forEach { star ->
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Estrella",
                        tint = if (star <= review.rating) Color(0xFFFFD700) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            if (review.imageUris.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 8.dp)
                ) {
                    review.imageUris.forEach { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Imagen de reseña",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onImageClick(uri) },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
