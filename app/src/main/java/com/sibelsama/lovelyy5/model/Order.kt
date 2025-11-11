package com.sibelsama.lovelyy5.model

data class ShippingDetails(
    val rut: String,
    val names: String,
    val lastNames: String,
    val phone: String,
    val email: String,
    val address: String,
    val region: String
)

data class Order(
    val id: String,
    val shippingDetails: ShippingDetails,
    val items: Map<Product, Int>,
    val subtotal: Double,
    val shippingCost: Double,
    val total: Double
)