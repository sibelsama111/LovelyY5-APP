package com.sibelsama.lovelyy5.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductReview(
    val productId: Int,
    val name: String,
    val comment: String,
    val rating: Int,
    val imageUris: List<String> = emptyList()
)
