package com.sibelsama.lovelyy5.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sibelsama.lovelyy5.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

class OrderRepository(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "orders")
        private val ORDERS_KEY = stringPreferencesKey("orders_list")
    }

    fun getOrders(): Flow<List<Order>> = context.dataStore.data.map { prefs ->
        val raw = prefs[ORDERS_KEY]
        if (raw != null) {
            try {
                Json.decodeFromString<List<Order>>(raw)
            } catch (_: Exception) {
                emptyList()
            }
        } else emptyList()
    }

    suspend fun saveOrder(order: Order) {
        context.dataStore.edit { prefs ->
            val currentRaw = prefs[ORDERS_KEY]
            val current = if (currentRaw != null) {
                try {
                    Json.decodeFromString<List<Order>>(currentRaw)
                } catch (_: Exception) {
                    emptyList()
                }
            } else emptyList()
            val updated = current + order
            prefs[ORDERS_KEY] = Json.encodeToString(updated)
        }
    }

    suspend fun clearOrders() {
        context.dataStore.edit { prefs ->
            prefs.remove(ORDERS_KEY)
        }
    }

    suspend fun getOrderById(id: String): Order? {
        return context.dataStore.data
            .map { prefs ->
                val raw = prefs[ORDERS_KEY]
                if (raw != null) {
                    try {
                        Json.decodeFromString<List<Order>>(raw)
                    } catch (_: Exception) {
                        emptyList()
                    }
                } else emptyList()
            }
            .map { list -> list.find { it.id == id } }
            .firstOrNull()
    }
}
