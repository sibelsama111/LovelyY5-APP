package com.sibelsama.lovelyy5.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @param:PrimaryKey val id: String,
    val shippingDetails: String, // JSON
    val items: String, // JSON map productId->quantity
    val subtotal: Double,
    val shippingCost: Double,
    val total: Double
)
