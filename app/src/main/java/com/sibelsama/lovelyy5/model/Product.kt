package com.sibelsama.lovelyy5.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    // Campos opcionales para mapear el JSON de productos
    val category: String = "",
    val images: List<String> = emptyList(),
    val stock: Int = 0,
    val specs: String = ""
)
