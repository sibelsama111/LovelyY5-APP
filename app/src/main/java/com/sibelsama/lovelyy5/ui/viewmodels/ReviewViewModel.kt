package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sibelsama.lovelyy5.model.ProductReview
import com.sibelsama.lovelyy5.repository.ReviewRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReviewViewModel(
    application: Application,
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(kotlinx.serialization.json.Json { ignoreUnknownKeys = true })
        }
    }
) : AndroidViewModel(application) {
    private val repository = ReviewRepository(application.applicationContext)

    private val reviewFlows: MutableMap<Int, StateFlow<List<ProductReview>>> = mutableMapOf()
    val newReviewImages = MutableStateFlow<List<String>>(emptyList())

    fun getReviews(productId: Int): StateFlow<List<ProductReview>> {
        return reviewFlows.getOrPut(productId) {
            repository.getReviews(productId).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
        }
    }

    fun addImageToNewReview(imageUri: String) {
        newReviewImages.value = newReviewImages.value + imageUri
    }

    fun removeImageFromNewReview(imageUri: String) {
        newReviewImages.value = newReviewImages.value - imageUri
    }

    fun clearNewReviewImages() {
        newReviewImages.value = emptyList()
    }


    fun fetchRandomCatImage() {
        viewModelScope.launch {
            try {
                val response: List<com.sibelsama.lovelyy5.ui.dopamina.CatImage> = client.get("https://api.thecatapi.com/v1/images/search").body()
                response.firstOrNull()?.url?.let {
                    addImageToNewReview(it)
                }
            } catch (e: Exception) {
                android.util.Log.e("ReviewViewModel", "Error fetching cat image", e)
            }
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
