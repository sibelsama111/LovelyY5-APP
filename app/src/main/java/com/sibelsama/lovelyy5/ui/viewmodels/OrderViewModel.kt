package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.model.ShippingDetails
import com.sibelsama.lovelyy5.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OrderRepository(application.applicationContext)
    val orders: StateFlow<List<Order>> = repository.getOrders().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun createOrder(cartItems: Map<Product, Int>, shippingDetails: ShippingDetails, shippingFee: Double = 5000.0) {
        val orderId = UUID.randomUUID().toString().take(6)
        val subtotal = cartItems.entries.sumOf { (product, quantity) -> product.price * quantity }
        val newOrder = Order(
            id = "#${orderId}",
            // Convert items to Map<productId, quantity> to match Order model
            items = cartItems.mapKeys { it.key.id },
            shippingDetails = shippingDetails,
            subtotal = subtotal,
            shippingCost = shippingFee,
            total = subtotal + shippingFee
        )
        viewModelScope.launch {
            repository.saveOrder(newOrder)
        }
    }

    fun clearOrders() {
        viewModelScope.launch {
            repository.clearOrders()
        }
    }

    suspend fun getOrderById(orderId: String): Order? {
        return repository.getOrderById(orderId)
    }
}
