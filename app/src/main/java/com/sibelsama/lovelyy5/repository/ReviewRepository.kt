package com.sibelsama.lovelyy5.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sibelsama.lovelyy5.data.FirebaseService
import com.sibelsama.lovelyy5.model.ProductReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ReviewRepository(
    private val context: Context,
    private val firebaseService: FirebaseService = FirebaseService()
) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reviews")
        private val REVIEWS_KEY = stringPreferencesKey("reviews_list")
    }

    private var useFirebase = true

    fun getReviews(productId: Int): Flow<List<ProductReview>> = flow {
        if (useFirebase) {
            val reviews = firebaseService.getReviewsByProductId(productId)
            if (reviews.isNotEmpty()) {
                emit(reviews)
                return@flow
            }
        }
        // Fallback a DataStore local
        context.dataStore.data.map { prefs ->
            val raw = prefs[REVIEWS_KEY]
            if (raw != null) {
                try {
                    val allReviews = Json.decodeFromString<List<ProductReview>>(raw)
                    val filteredReviews = allReviews.filter { r -> r.productId == productId }
                    android.util.Log.d("ReviewRepository", "Retrieved ${filteredReviews.size} reviews from local for product $productId")
                    filteredReviews
                } catch (e: Exception) {
                    android.util.Log.e("ReviewRepository", "Error decoding reviews", e)
                    emptyList()
                }
            } else {
                android.util.Log.d("ReviewRepository", "No reviews found in storage")
                emptyList()
            }
        }.collect { emit(it) }
    }

    suspend fun getAllReviews(): List<ProductReview> {
        return if (useFirebase) {
            firebaseService.getAllReviews()
        } else {
            emptyList()
        }
    }

    suspend fun saveReview(review: ProductReview): Result<String> {
        return try {
            if (useFirebase) {
                val result = firebaseService.addReview(review)
                if (result.isSuccess) {
                    android.util.Log.d("ReviewRepository", "Review saved to Firebase: ${result.getOrNull()}")
                    return result
                }
            }

            // Fallback a DataStore local
            context.dataStore.edit { prefs ->
                val currentRaw = prefs[REVIEWS_KEY]
                val current = if (currentRaw != null) {
                    try {
                        Json.decodeFromString<List<ProductReview>>(currentRaw)
                    } catch (e: Exception) {
                        android.util.Log.e("ReviewRepository", "Error decoding existing reviews", e)
                        emptyList()
                    }
                } else {
                    emptyList()
                }

                val updated = current + review
                val encodedData = Json.encodeToString(updated)
                prefs[REVIEWS_KEY] = encodedData

                android.util.Log.d("ReviewRepository", "Review saved locally. Total reviews: ${updated.size}")
            }
            Result.success("local")
        } catch (e: Exception) {
            android.util.Log.e("ReviewRepository", "Error saving review", e)
            Result.failure(e)
        }
    }

    suspend fun updateReview(reviewId: String, updates: Map<String, Any>): Result<Unit> {
        return firebaseService.updateReview(reviewId, updates)
    }

    suspend fun deleteReview(reviewId: String): Result<Unit> {
        return firebaseService.deleteReview(reviewId)
    }

    fun setUseFirebase(enabled: Boolean) {
        useFirebase = enabled
    }
}
