package com.sibelsama.lovelyy5.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductReview(
    val productId: Int,
    val name: String, // También conocido como userName en Firebase
    val comment: String,
    val rating: Int,
    val imageUris: List<String> = emptyList()
) {
    // Propiedad de conveniencia para compatibilidad con Firebase
    val userName: String get() = name
}
