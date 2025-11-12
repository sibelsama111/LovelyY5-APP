package com.sibelsama.lovelyy5.repository

import android.content.Context
import com.sibelsama.lovelyy5.data.db.AppDatabase
import com.sibelsama.lovelyy5.data.db.OrderEntity
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.ShippingDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OrderRepository(private val context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val dao = db.orderDao()

    fun getOrders(): Flow<List<Order>> = dao.getAllOrders().map { list ->
        list.map { ent ->
            val shipping = try { Json.decodeFromString<ShippingDetails>(ent.shippingDetails) } catch (_: Exception) { ShippingDetails("","","","","","","") }
            val items = try { Json.decodeFromString<Map<Int, Int>>(ent.items) } catch (_: Exception) { emptyMap() }
            Order(
                id = ent.id,
                shippingDetails = shipping,
                items = items,
                subtotal = ent.subtotal,
                shippingCost = ent.shippingCost,
                total = ent.total
            )
        }
    }

    suspend fun saveOrder(order: Order) {
        val ent = OrderEntity(
            id = order.id,
            shippingDetails = Json.encodeToString(order.shippingDetails),
            items = Json.encodeToString(order.items),
            subtotal = order.subtotal,
            shippingCost = order.shippingCost,
            total = order.total
        )
        dao.insert(ent)
    }

    suspend fun clearOrders() {
        dao.clearAll()
    }

    suspend fun getOrderById(id: String): Order? {
        val ent = dao.getOrderById(id) ?: return null
        val shipping = try { Json.decodeFromString<ShippingDetails>(ent.shippingDetails) } catch (_: Exception) { ShippingDetails("","","","","","","") }
        val items = try { Json.decodeFromString<Map<Int, Int>>(ent.items) } catch (_: Exception) { emptyMap() }
        return Order(
            id = ent.id,
            shippingDetails = shipping,
            items = items,
            subtotal = ent.subtotal,
            shippingCost = ent.shippingCost,
            total = ent.total
        )
    }
}
