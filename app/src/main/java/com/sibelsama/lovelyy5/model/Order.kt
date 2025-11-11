package com.sibelsama.lovelyy5.model

import kotlinx.serialization.Serializable

@Serializable
data class ShippingDetails(
    val rut: String,
    val names: String,
    val lastNames: String,
    val phone: String,
    val email: String,
    val address: String,
    val region: String
)

/**
 * Nota: items utiliza Map<productId, cantidad> para facilitar la serialización y el almacenamiento.
 */
@Serializable
data class Order(
    val id: String,
    val shippingDetails: ShippingDetails,
    val items: Map<Int, Int>,
    val subtotal: Double,
    val shippingCost: Double,
    val total: Double
)