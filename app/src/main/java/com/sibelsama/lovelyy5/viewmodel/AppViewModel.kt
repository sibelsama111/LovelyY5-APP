package com.sibelsama.lovelyy5.viewmodel

import androidx.lifecycle.ViewModel
import com.sibelsama.lovelyy5.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel ligero que sólo mantiene un catálogo local y un carrito en memoria.
 * Las operaciones persistentes se manejan en los repositorios dentro de los ViewModels de UI.
 */
class AppViewModel : ViewModel() {
    val productList = listOf(
        Product(1, "Notebook HP", "Notebook 15,6\" Intel i5, 8GB, 256GB SSD", 499900.0),
        Product(2, "Mouse Logitech", "Mouse inalámbrico, ergonómico, USB", 19990.0),
        Product(3, "Smartphone Samsung", "Galaxy A14, 128GB, Dual SIM", 179990.0)
    )

    private val _cart = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cart: StateFlow<Map<Product, Int>> = _cart.asStateFlow()

    fun addToCart(product: Product) {
        val currentCart = _cart.value.toMutableMap()
        currentCart[product] = (currentCart[product] ?: 0) + 1
        _cart.value = currentCart
    }

    fun removeFromCart(product: Product) {
        val currentCart = _cart.value.toMutableMap()
        currentCart.remove(product)
        _cart.value = currentCart
    }

    fun updateQuantity(product: Product, quantity: Int) {
        val currentCart = _cart.value.toMutableMap()
        if (quantity > 0) {
            currentCart[product] = quantity
        } else {
            currentCart.remove(product)
        }
        _cart.value = currentCart
    }
}
