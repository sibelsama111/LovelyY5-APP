package com.sibelsama.lovelyy5.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.model.ShippingDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class OrderViewModel : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    fun createOrder(cartItems: Map<Product, Int>, shippingDetails: ShippingDetails) {
        val orderId = UUID.randomUUID().toString()
        val newOrder = Order(
            id = orderId,
            items = cartItems,
            shippingDetails = shippingDetails,
            totalAmount = cartItems.entries.sumOf { (product, quantity) -> product.price * quantity }
        )
        _orders.value = _orders.value + newOrder
    }

    fun getOrderById(orderId: String): Order? {
        return _orders.value.find { it.id == orderId }
    }
}
