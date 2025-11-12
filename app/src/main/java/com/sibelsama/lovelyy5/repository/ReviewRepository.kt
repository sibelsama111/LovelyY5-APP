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

class ReviewRepository(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reviews")
        private val REVIEWS_KEY = stringPreferencesKey("reviews_list")
    }

    fun getReviews(productId: Int): Flow<List<ProductReview>> = context.dataStore.data.map { prefs ->
        val raw = prefs[REVIEWS_KEY]
        if (raw != null) {
            try {
                Json.decodeFromString<List<ProductReview>>(raw).filter { r -> r.productId == productId }
            } catch (_: Exception) {
                emptyList()
            }
        } else emptyList()
    }

    suspend fun saveReview(review: ProductReview) {
        context.dataStore.edit { prefs ->
            val currentRaw = prefs[REVIEWS_KEY]
            val current = if (currentRaw != null) {
                try {
                    Json.decodeFromString<List<ProductReview>>(currentRaw)
                } catch (_: Exception) {
                    emptyList()
                }
            } else emptyList()
            val updated = current + review
            prefs[REVIEWS_KEY] = Json.encodeToString(updated)
        }
    }
}
