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
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class OrderRepository(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "orders")
        private val ORDERS_KEY = stringPreferencesKey("orders_list")
    }

    fun getOrders(): Flow<List<Order>> = context.dataStore.data.map { prefs ->
        val raw = prefs[ORDERS_KEY]
        if (raw != null) {
            try {
                val allOrders = Json.decodeFromString<List<Order>>(raw)
                android.util.Log.d("OrderRepository", "Retrieved ${allOrders.size} orders")
                allOrders
            } catch (e: Exception) {
                android.util.Log.e("OrderRepository", "Error decoding orders", e)
                emptyList()
            }
        } else {
            android.util.Log.d("OrderRepository", "No orders found in storage")
            emptyList()
        }
    }

    suspend fun saveOrder(order: Order) {
        try {
            context.dataStore.edit { prefs ->
                val currentRaw = prefs[ORDERS_KEY]
                val current = if (currentRaw != null) {
                    try {
                        Json.decodeFromString<List<Order>>(currentRaw)
                    } catch (e: Exception) {
                        android.util.Log.e("OrderRepository", "Error decoding existing orders", e)
                        emptyList()
                    }
                } else {
                    emptyList()
                }

                val updated = current + order
                val encodedData = Json.encodeToString(updated)
                prefs[ORDERS_KEY] = encodedData

                android.util.Log.d("OrderRepository", "Order saved successfully. Total orders: ${updated.size}")
            }
        } catch (e: Exception) {
            android.util.Log.e("OrderRepository", "Error saving order", e)
            throw e
        }
    }
}
