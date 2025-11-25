package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OrderRepository(application.applicationContext)

    val orders: StateFlow<List<Order>> = repository.getOrders().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun saveOrder(order: Order) {
        viewModelScope.launch {
            try {
                repository.saveOrder(order)
                android.util.Log.d("OrderViewModel", "Order saved successfully")
            } catch (e: Exception) {
                android.util.Log.e("OrderViewModel", "Error saving order", e)
            }
        }
    }
}

