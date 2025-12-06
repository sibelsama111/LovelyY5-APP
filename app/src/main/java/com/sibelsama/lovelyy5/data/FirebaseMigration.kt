package com.sibelsama.lovelyy5.data

import android.content.Context
import com.sibelsama.lovelyy5.model.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * Utilidad para migrar datos locales a Firebase
 * Usar esta clase para poblar Firebase con los datos iniciales
 */
class FirebaseMigration(
    private val context: Context,
    private val firebaseService: FirebaseService = FirebaseService()
) {

    /**
     * Migra todos los productos desde assets/products.json a Firebase
     * Retorna el número de productos migrados exitosamente
     */
    suspend fun migrateProductsToFirebase(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            // Leer productos desde assets
            val input = context.assets.open("products.json")
            val json = input.bufferedReader().use { it.readText() }
            val products = Json.decodeFromString<List<ProductItem>>(json)

            android.util.Log.d("FirebaseMigration", "Found ${products.size} products to migrate")

            var successCount = 0
            var failCount = 0

            // Subir cada producto a Firebase
            products.forEach { product ->
                val result = firebaseService.addProduct(product)
                if (result.isSuccess) {
                    successCount++
                    android.util.Log.d("FirebaseMigration", "Migrated product: ${product.nombre}")
                } else {
                    failCount++
                    android.util.Log.e("FirebaseMigration", "Failed to migrate product: ${product.nombre}", result.exceptionOrNull())
                }
            }

            android.util.Log.d("FirebaseMigration", "Migration complete: $successCount succeeded, $failCount failed")
            Result.success(successCount)

        } catch (e: Exception) {
            android.util.Log.e("FirebaseMigration", "Error during migration", e)
            Result.failure(e)
        }
    }

    /**
     * Verifica si Firebase ya tiene productos
     */
    suspend fun hasProductsInFirebase(): Boolean = withContext(Dispatchers.IO) {
        try {
            val products = firebaseService.getProducts()
            products.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Migra automáticamente si Firebase está vacío
     */
    suspend fun migrateIfEmpty(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            if (!hasProductsInFirebase()) {
                android.util.Log.d("FirebaseMigration", "Firebase is empty, starting migration...")
                val result = migrateProductsToFirebase()
                if (result.isSuccess) {
                    Result.success(true)
                } else {
                    Result.failure(result.exceptionOrNull() ?: Exception("Migration failed"))
                }
            } else {
                android.util.Log.d("FirebaseMigration", "Firebase already has products, skipping migration")
                Result.success(false)
            }
        } catch (e: Exception) {
            android.util.Log.e("FirebaseMigration", "Error checking migration", e)
            Result.failure(e)
        }
    }
}

