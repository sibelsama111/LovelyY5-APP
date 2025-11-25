package com.sibelsama.lovelyy5.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sibelsama.lovelyy5.model.ProductReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ReviewRepository(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reviews")
        private val REVIEWS_KEY = stringPreferencesKey("reviews_list")
    }

    fun getReviews(productId: Int): Flow<List<ProductReview>> = context.dataStore.data.map { prefs ->
        val raw = prefs[REVIEWS_KEY]
        if (raw != null) {
            try {
                val allReviews = Json.decodeFromString<List<ProductReview>>(raw)
                val filteredReviews = allReviews.filter { r -> r.productId == productId }
                android.util.Log.d("ReviewRepository", "Retrieved ${filteredReviews.size} reviews for product $productId")
                filteredReviews
            } catch (e: Exception) {
                android.util.Log.e("ReviewRepository", "Error decoding reviews", e)
                emptyList()
            }
        } else {
            android.util.Log.d("ReviewRepository", "No reviews found in storage")
            emptyList()
        }
    }



    suspend fun saveReview(review: ProductReview) {
        try {
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

                android.util.Log.d("ReviewRepository", "Review saved successfully. Total reviews: ${updated.size}")
            }
        } catch (e: Exception) {
            android.util.Log.e("ReviewRepository", "Error saving review", e)
            throw e
        }
    }
}
