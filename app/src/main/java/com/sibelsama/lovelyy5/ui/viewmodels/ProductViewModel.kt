package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sibelsama.lovelyy5.model.ProductItem
import com.sibelsama.lovelyy5.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProductRepository(application.applicationContext)

    private val _products = MutableStateFlow<List<ProductItem>>(emptyList())
    val products: StateFlow<List<ProductItem>> = _products.asStateFlow()

    init {
        viewModelScope.launch {
            val loaded = repository.loadProducts()
            _products.value = loaded
            android.util.Log.d("ProductViewModel", "Loaded ${loaded.size} products")
        }
    }

    fun getByCategory(tipo: String?): List<ProductItem> {
        return if (tipo.isNullOrBlank()) {
            _products.value
        } else {
            _products.value.filter { it.tipo.equals(tipo, ignoreCase = true) }
        }
    }
}

