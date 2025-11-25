package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sibelsama.lovelyy5.data.DataManager
import com.sibelsama.lovelyy5.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val dataManager = DataManager(application.applicationContext)
    private val _cartItems = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cartItems: StateFlow<Map<Product, Int>> = _cartItems.asStateFlow()

    init {
        // Cargar datos del carrito al inicializar
        loadCartFromStorage()
    }

    private fun loadCartFromStorage() {
        viewModelScope.launch {
            try {
                dataManager.cartItems.collect { cartJson ->
                    if (cartJson.isNotBlank()) {
                        try {
                            val cartMap = Json.decodeFromString<Map<String, CartItem>>(cartJson)
                            val restoredCart = cartMap.values.associate { item ->
                                Product(
                                    id = item.productId,
                                    name = item.productName,
                                    description = item.productDescription,
                                    price = item.productPrice,
                                    category = item.productCategory,
                                    stock = item.productStock
                                ) to item.quantity
                            }
                            _cartItems.value = restoredCart
                        } catch (e: Exception) {
                            // Si hay error al deserializar, mantener carrito vacío
                        }
                    }
                }
            } catch (e: Exception) {
                // Manejo silencioso del error
            }
        }
    }

    private fun saveCartToStorage() {
        viewModelScope.launch {
            try {
                val cartItems = _cartItems.value
                val cartData = cartItems.map { (product, quantity) ->
                    product.id.toString() to CartItem(
                        productId = product.id,
                        productName = product.name,
                        productDescription = product.description,
                        productPrice = product.price,
                        productCategory = product.category,
                        productStock = product.stock,
                        quantity = quantity
                    )
                }.toMap()

                val cartJson = Json.encodeToString(cartData)
                dataManager.saveCartItems(cartJson)
            } catch (e: Exception) {
                // Manejo silencioso del error
            }
        }
    }

    // Clase auxiliar para serialización
    @kotlinx.serialization.Serializable
    private data class CartItem(
        val productId: Int,
        val productName: String,
        val productDescription: String,
        val productPrice: Double,
        val productCategory: String,
        val productStock: Int,
        val quantity: Int
    )

    fun addToCart(product: Product) {
        try {
            val currentItems = _cartItems.value.toMutableMap()
            val currentQuantity = currentItems.getOrDefault(product, 0)

            // Validar stock disponible
            if (currentQuantity < product.stock) {
                currentItems[product] = currentQuantity + 1
                _cartItems.value = currentItems
                saveCartToStorage()
            }
            // Si no hay stock suficiente, no hacer nada (podría lanzar excepción si se desea)
        } catch (e: Exception) {
            // En caso de error, no crashear la app
            // Se podría agregar logging aquí
        }
    }

    fun removeFromCart(product: Product) {
        try {
            val currentItems = _cartItems.value.toMutableMap()
            currentItems.remove(product)
            _cartItems.value = currentItems
            saveCartToStorage()
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    fun updateQuantity(product: Product, newQuantity: Int) {
        try {
            when {
                newQuantity <= 0 -> removeFromCart(product)
                newQuantity <= product.stock -> {
                    val currentItems = _cartItems.value.toMutableMap()
                    currentItems[product] = newQuantity
                    _cartItems.value = currentItems
                    saveCartToStorage()
                }
                // Si la cantidad excede el stock, usar el stock máximo disponible
                else -> {
                    val currentItems = _cartItems.value.toMutableMap()
                    currentItems[product] = product.stock
                    _cartItems.value = currentItems
                    saveCartToStorage()
                }
            }
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    fun clearCart() {
        try {
            _cartItems.value = emptyMap()
            saveCartToStorage()
        } catch (e: Exception) {
            // Manejo de error silencioso
        }
    }

    fun getCartItemCount(): Int {
        return try {
            _cartItems.value.values.sum()
        } catch (e: Exception) {
            0
        }
    }

    fun getCartTotal(): Double {
        return try {
            _cartItems.value.entries.sumOf { (product, quantity) ->
                product.price * quantity
            }
        } catch (e: Exception) {
            0.0
        }
    }
}
