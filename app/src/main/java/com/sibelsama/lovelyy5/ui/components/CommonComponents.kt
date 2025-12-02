package com.sibelsama.lovelyy5.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sibelsama.lovelyy5.R

@Composable
fun ProductImage(imagePath: String?, contentDescription: String?, modifier: Modifier = Modifier) {
    val imageModel = when {
        imagePath.isNullOrBlank() -> R.drawable.ic_launcher_background // Fallback image
        imagePath.startsWith("http") -> imagePath // It's a URL
        else -> {
            // It's a local asset, construct the correct path
            val assetRelative = when {
                imagePath.contains("/assets/") -> imagePath.substringAfter("/assets/")
                imagePath.startsWith("assets/") -> imagePath.substringAfter("assets/")
                imagePath.startsWith("/") -> imagePath.trimStart('/')
                else -> imagePath
            }
            "file:///android_asset/$assetRelative"
        }
    }

    AsyncImage(
        model = imageModel,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        placeholder = rememberAsyncImagePainter(model = R.drawable.ic_launcher_foreground),
        error = rememberAsyncImagePainter(model = R.drawable.ic_launcher_background)
    )
}

