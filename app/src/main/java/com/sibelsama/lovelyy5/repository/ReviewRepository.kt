package com.sibelsama.lovelyy5.repository

import android.content.Context
import com.sibelsama.lovelyy5.data.db.AppDatabase
import com.sibelsama.lovelyy5.data.db.ReviewEntity
import com.sibelsama.lovelyy5.model.ProductReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class ReviewRepository(private val context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val dao = db.reviewDao()

    fun getReviews(productId: Int): Flow<List<ProductReview>> = dao.getReviewsForProduct(productId).map { list ->
        list.mapNotNull { ent ->
            try {
                ProductReview(
                    productId = ent.productId,
                    name = ent.name.take(100), // Limitar longitud del nombre
                    comment = ent.comment.take(500), // Limitar longitud del comentario
                    rating = ent.rating.coerceIn(1, 5), // Asegurar que el rating esté en rango válido
                    imageUris = try {
                        val uris = Json.decodeFromString<List<String>>(ent.imageUrisJson)
                        uris.take(5) // Limitar a máximo 5 imágenes
                    } catch (_: Exception) {
                        emptyList()
                    }
                )
            } catch (e: Exception) {
                // Si hay error procesando una review, la omitimos pero no crasheamos
                null
            }
        }
    }

    suspend fun saveReview(review: ProductReview) {
        try {
            // Validaciones antes de guardar
            if (review.name.isBlank() || review.comment.isBlank()) {
                throw IllegalArgumentException("Nombre y comentario son requeridos")
            }

            if (review.rating !in 1..5) {
                throw IllegalArgumentException("Rating debe estar entre 1 y 5")
            }

            val sanitizedImageUris = review.imageUris.take(5).filter { it.isNotBlank() }
            val jsonUris = Json.encodeToString(sanitizedImageUris)

            val ent = ReviewEntity(
                productId = review.productId,
                name = review.name.trim().take(100),
                comment = review.comment.trim().take(500),
                rating = review.rating,
                imageUrisJson = jsonUris
            )
            dao.insert(ent)
        } catch (e: Exception) {
            throw Exception("Error al guardar la valoración: ${e.message}")
        }
    }
}
