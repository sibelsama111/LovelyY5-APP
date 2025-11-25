package com.sibelsama.lovelyy5.repository

import android.content.Context
import com.sibelsama.lovelyy5.model.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class ProductRepository(private val context: Context) {
    suspend fun loadProducts(): List<ProductItem> = withContext(Dispatchers.IO) {
        try {
            val input = context.assets.open("products.json")
            val json = input.bufferedReader().use { it.readText() }
            Json.decodeFromString<List<ProductItem>>(json)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "Error loading products.json", e)
            emptyList()
        }
    }
}
