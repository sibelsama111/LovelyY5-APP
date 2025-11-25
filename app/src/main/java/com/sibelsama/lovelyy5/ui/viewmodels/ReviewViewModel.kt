package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sibelsama.lovelyy5.model.ProductReview
import com.sibelsama.lovelyy5.repository.ReviewRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReviewViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ReviewRepository(application.applicationContext)

    // Cachear StateFlows por productId para evitar crear uno nuevo en cada getReviews()
    private val reviewFlows: MutableMap<Int, StateFlow<List<ProductReview>>> = mutableMapOf()

    fun getReviews(productId: Int): StateFlow<List<ProductReview>> {
        return reviewFlows.getOrPut(productId) {
            repository.getReviews(productId).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
        }
    }

    fun saveReview(review: ProductReview) {
        viewModelScope.launch {
            try {
                repository.saveReview(review)
                android.util.Log.d("ReviewViewModel", "Review saved successfully")
            } catch (e: Exception) {
                android.util.Log.e("ReviewViewModel", "Error saving review", e)
            }
        }
    }
}
