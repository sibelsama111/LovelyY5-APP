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

    fun getReviews(productId: Int): StateFlow<List<ProductReview>> =
        repository.getReviews(productId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun saveReview(review: ProductReview) {
        viewModelScope.launch {
            repository.saveReview(review)
        }
    }
}
