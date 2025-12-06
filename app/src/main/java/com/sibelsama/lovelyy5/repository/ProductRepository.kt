package com.sibelsama.lovelyy5.repository

import android.content.Context
import com.sibelsama.lovelyy5.data.FirebaseService
import com.sibelsama.lovelyy5.model.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class ProductRepository(
    private val context: Context,
    private val firebaseService: FirebaseService = FirebaseService()
) {
    private var useFirebase = true

    suspend fun loadProducts(): List<ProductItem> = withContext(Dispatchers.IO) {
        if (useFirebase) {
            val products = firebaseService.getProducts()
            if (products.isNotEmpty()) {
                return@withContext products
            }
        }
        // Fallback a productos locales si Firebase falla o está vacío
        loadProductsFromAssets()
    }

    suspend fun getProductById(productId: Int): ProductItem? = withContext(Dispatchers.IO) {
        if (useFirebase) {
            firebaseService.getProductById(productId)
        } else {
            loadProductsFromAssets().find { it.id == productId }
        }
    }

    suspend fun addProduct(product: ProductItem): Result<String> = withContext(Dispatchers.IO) {
        firebaseService.addProduct(product)
    }

    suspend fun updateProduct(productId: Int, updates: Map<String, Any>): Result<Unit> = withContext(Dispatchers.IO) {
        firebaseService.updateProduct(productId, updates)
    }

    suspend fun deleteProduct(productId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        firebaseService.deleteProduct(productId)
    }

    private fun loadProductsFromAssets(): List<ProductItem> {
        return try {
            val input = context.assets.open("products.json")
            val json = input.bufferedReader().use { it.readText() }
            Json.decodeFromString<List<ProductItem>>(json)
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "Error loading products.json", e)
            emptyList()
        }
    }

    fun setUseFirebase(enabled: Boolean) {
        useFirebase = enabled
    }
}
