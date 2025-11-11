package com.sibelsama.lovelyy5.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.sibelsama.lovelyy5.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cartItems: StateFlow<Map<Product, Int>> = _cartItems.asStateFlow()

    fun addToCart(product: Product) {
        val currentItems = _cartItems.value.toMutableMap()
        val currentQuantity = currentItems.getOrDefault(product, 0)
        currentItems[product] = currentQuantity + 1
        _cartItems.value = currentItems
    }

    fun removeFromCart(product: Product) {
        val currentItems = _cartItems.value.toMutableMap()
        currentItems.remove(product)
        _cartItems.value = currentItems
    }

    fun updateQuantity(product: Product, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(product)
        } else {
            val currentItems = _cartItems.value.toMutableMap()
            currentItems[product] = newQuantity
            _cartItems.value = currentItems
        }
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
    }
}
