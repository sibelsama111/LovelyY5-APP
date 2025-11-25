package com.sibelsama.lovelyy5.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductItem(
    val id: Int,
    val codigo: String,
    val createdAt: String,
    val descripcion: String,
    val detalles: Map<String, String> = emptyMap(),
    val imagenes: List<String> = emptyList(),
    val marca: String,
    val nombre: String,
    val precioActual: Double,
    val precioOriginal: Double,
    val stock: Int,
    val tipo: String
)

