package com.sibelsama.lovelyy5.repository

import com.sibelsama.lovelyy5.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderRepository {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private var lastOrderId = 0

    fun addOrder(order: Order) {
        lastOrderId++
        val newOrder = order.copy(id = lastOrderId.toString().padStart(5, '0'))
        _orders.value = _orders.value + newOrder
    }

    fun getOrderById(id: String): Order? {
        return _orders.value.find { it.id == id }
    }
}
