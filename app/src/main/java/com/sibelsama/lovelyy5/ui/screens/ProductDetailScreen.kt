package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.Image
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.model.Product
import androidx.compose.ui.platform.LocalContext
import android.os.VibrationEffect
import android.os.Vibrator
import com.sibelsama.lovelyy5.model.ProductReview
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel

@Composable
fun ProductDetailScreen(
    product: Product,
    reviewViewModel: ReviewViewModel,
    onAddToCart: () -> Unit
) {
    val reviews by reviewViewModel.getReviews(product.id).collectAsState()
    var name by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(1) }
    var images by remember { mutableStateOf(listOf<Uri>()) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            // Guardar imagen en almacenamiento local si se requiere persistencia
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        images = images + uris
    }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F3F8))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* back navigation */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Celulares", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(product.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 16.dp))
        Text(product.description, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp))
        Text("$${product.price.toInt()} CLP", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            repeat(4) {
                Image(
                    painter = painterResource(id = com.sibelsama.lovelyy5.R.drawable.ic_launcher_background),
                    contentDescription = "Foto producto",
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.let {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        it.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                    } else {
                        @Suppress("DEPRECATION")
                        it.vibrate(50)
                    }
                }
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
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Text("Deje su valoración", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 16.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        OutlinedTextField(value = comment, onValueChange = { comment = it }, label = { Text("Opinión del producto") }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.let {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        it.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                    } else {
                        @Suppress("DEPRECATION")
                        it.vibrate(50)
                    }
                }
                cameraLauncher.launch(null)
            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB36AE2))) {
                Text("Cámara", color = Color.White)
            }
            Button(onClick = {
                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.let {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        it.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                    } else {
                        @Suppress("DEPRECATION")
                        it.vibrate(50)
                    }
                }
                galleryLauncher.launch("image/*")
            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB36AE2))) {
                Text("Galería", color = Color.White)
            }
        }
        if (images.isNotEmpty()) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
                images.forEach { uri ->
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        Image(painter = rememberAsyncImagePainter(uri), contentDescription = "Imagen valoración", modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)))
                        IconButton(
                            onClick = {
                                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                                vibrator?.let {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        it.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                                    } else {
                                        @Suppress("DEPRECATION")
                                        it.vibrate(50)
                                    }
                                }
                                images = images.filter { it != uri }
                            },
                            modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar imagen", tint = Color.Red)
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
                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.let {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        it.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        @Suppress("DEPRECATION")
                        it.vibrate(2000)
                    }
                }
                if (name.isNotBlank() && comment.isNotBlank()) {
                    reviewViewModel.saveReview(
                        ProductReview(
                            productId = product.id,
                            name = name,
                            comment = comment,
                            rating = rating,
                            imageUris = images.map { it.toString() }
                        )
                    )
                    name = ""
                    comment = ""
                    rating = 1
                    images = emptyList()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB36AE2))
        ) {
            Text("Valorar", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Text("Valoraciones", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 16.dp))
        reviews.forEach { review ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(review.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.width(8.dp))
                    repeat(review.rating) {
                        Icon(Icons.Default.Star, contentDescription = "Estrella", tint = Color(0xFFB36AE2))
                    }
                }
                Text(review.comment, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
                if (review.imageUris.isNotEmpty()) {
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(start = 8.dp, bottom = 8.dp)) {
                        review.imageUris.forEach { uriStr ->
                            val uri = android.net.Uri.parse(uriStr)
                            Image(painter = rememberAsyncImagePainter(uri), contentDescription = "Imagen valoración", modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)).padding(end = 8.dp))
                        }
                    }
                }
            }
        }
    }
}
