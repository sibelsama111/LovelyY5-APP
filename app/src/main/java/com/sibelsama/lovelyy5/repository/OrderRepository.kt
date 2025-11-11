package com.sibelsama.lovelyy5.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sibelsama.lovelyy5.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OrderRepository(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "orders")
        private val ORDERS_KEY = preferencesKey<String>("orders_list")
    }

    fun getOrders(): Flow<List<Order>> = context.dataStore.data.map { prefs ->
        prefs[ORDERS_KEY]?.let {
            try {
                Json.decodeFromString<List<Order>>(it)
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }

    suspend fun saveOrder(order: Order) {
        context.dataStore.edit { prefs ->
            val current = prefs[ORDERS_KEY]?.let {
                try {
                    Json.decodeFromString<List<Order>>(it)
                } catch (e: Exception) {
                    emptyList()
                }
            } ?: emptyList()
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
        val orders = context.dataStore.data.map { prefs ->
            prefs[ORDERS_KEY]?.let {
                try {
                    Json.decodeFromString<List<Order>>(it)
                } catch (e: Exception) {
                    emptyList()
                }
            } ?: emptyList()
        }
        return orders.map { list -> list.find { it.id == id } }.firstOrNull()
    }
}
