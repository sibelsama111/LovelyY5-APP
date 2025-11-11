package com.sibelsama.lovelyy5.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sibelsama.lovelyy5.model.ProductReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReviewRepository(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reviews")
        private val REVIEWS_KEY = preferencesKey<String>("reviews_list")
    }

    fun getReviews(productId: Int): Flow<List<ProductReview>> = context.dataStore.data.map { prefs ->
        prefs[REVIEWS_KEY]?.let {
            try {
                Json.decodeFromString<List<ProductReview>>(it).filter { r -> r.productId == productId }
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }

    suspend fun saveReview(review: ProductReview) {
        context.dataStore.edit { prefs ->
            val current = prefs[REVIEWS_KEY]?.let {
                try {
                    Json.decodeFromString<List<ProductReview>>(it)
                } catch (e: Exception) {
                    emptyList()
                }
            } ?: emptyList()
            val updated = current + review
            prefs[REVIEWS_KEY] = Json.encodeToString(updated)
        }
    }
}
