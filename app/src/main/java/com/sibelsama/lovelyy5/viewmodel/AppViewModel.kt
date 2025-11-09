package com.sibelsama.lovelyy5.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.sibelsama.lovelyy5.model.Product

class AppViewModel : ViewModel() {
    val productList = listOf(
        Product(1, "Notebook HP", "Notebook 15,6\" Intel i5, 8GB, 256GB SSD", 499900.0),
        Product(2, "Mouse Logitech", "Mouse inalámbrico, ergonómico, USB", 19990.0),
        Product(3, "Smartphone Samsung", "Galaxy A14, 128GB, Dual SIM", 179990.0)
    )
    var cart = mutableStateListOf<Product>()
}
