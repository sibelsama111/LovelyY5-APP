package com.sibelsama.lovelyy5.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sibelsama.lovelyy5.data.FirebaseService
import com.sibelsama.lovelyy5.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class OrderRepository(
    private val context: Context,
    private val firebaseService: FirebaseService = FirebaseService()
) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "orders")
        private val ORDERS_KEY = stringPreferencesKey("orders_list")
    }

    private var useFirebase = true

    fun getOrders(): Flow<List<Order>> = flow {
        if (useFirebase) {
            val orders = firebaseService.getOrders()
            if (orders.isNotEmpty()) {
                emit(orders)
                return@flow
            }
        }
        // Fallback a DataStore local
        context.dataStore.data.map { prefs ->
            val raw = prefs[ORDERS_KEY]
            if (raw != null) {
                try {
                    val allOrders = Json.decodeFromString<List<Order>>(raw)
                    android.util.Log.d("OrderRepository", "Retrieved ${allOrders.size} orders from local")
                    allOrders
                } catch (e: Exception) {
                    android.util.Log.e("OrderRepository", "Error decoding orders", e)
                    emptyList()
                }
            } else {
                android.util.Log.d("OrderRepository", "No orders found in storage")
                emptyList()
            }
        }.collect { emit(it) }
    }

    suspend fun getOrderById(orderId: String): Order? {
        return if (useFirebase) {
            firebaseService.getOrderById(orderId)
        } else {
            null
        }
    }

    suspend fun saveOrder(order: Order): Result<String> {
        return try {
            if (useFirebase) {
                val result = firebaseService.createOrder(order)
                if (result.isSuccess) {
                    android.util.Log.d("OrderRepository", "Order saved to Firebase: ${result.getOrNull()}")
                    return result
                }
            }

            // Fallback a DataStore local
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

                android.util.Log.d("OrderRepository", "Order saved locally. Total orders: ${updated.size}")
            }
            Result.success(order.id)
        } catch (e: Exception) {
            android.util.Log.e("OrderRepository", "Error saving order", e)
            Result.failure(e)
        }
    }

    suspend fun updateOrder(orderId: String, updates: Map<String, Any>): Result<Unit> {
        return firebaseService.updateOrder(orderId, updates)
    }

    suspend fun deleteOrder(orderId: String): Result<Unit> {
        return firebaseService.deleteOrder(orderId)
    }

    fun setUseFirebase(enabled: Boolean) {
        useFirebase = enabled
    }
}
