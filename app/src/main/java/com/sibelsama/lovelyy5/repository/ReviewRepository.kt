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
        list.map { ent ->
            ProductReview(
                productId = ent.productId,
                name = ent.name,
                comment = ent.comment,
                rating = ent.rating,
                imageUris = try { Json.decodeFromString<List<String>>(ent.imageUrisJson) } catch (_: Exception) { emptyList() }
            )
        }
    }

    suspend fun saveReview(review: ProductReview) {
        val jsonUris = Json.encodeToString(review.imageUris)
        val ent = ReviewEntity(
            productId = review.productId,
            name = review.name,
            comment = review.comment,
            rating = review.rating,
            imageUrisJson = jsonUris
        )
        dao.insert(ent)
    }
}
