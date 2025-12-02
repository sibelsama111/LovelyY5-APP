package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import com.sibelsama.lovelyy5.model.ProductReview
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ReviewViewModelTest {

    private lateinit var reviewViewModel: ReviewViewModel
    private val application: Application = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveReview should execute without errors`() = runTest {
        reviewViewModel = ReviewViewModel(application)
        val review = ProductReview(1, "Tester", "Great product!", 5, emptyList())
        reviewViewModel.saveReview(review)
    }

    @Test
    fun `fetchRandomCatImage should add an image url to the list`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""[{"url":"https://example.com/cat.jpg"}]"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val mockClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(kotlinx.serialization.json.Json { ignoreUnknownKeys = true })
            }
        }

        reviewViewModel = ReviewViewModel(application, mockClient)

        assertEquals("Initial image list should be empty", 0, reviewViewModel.newReviewImages.value.size)

        reviewViewModel.fetchRandomCatImage()

        testDispatcher.scheduler.advanceUntilIdle()

        val newImages = reviewViewModel.newReviewImages.value
        assertEquals("Image list should contain one item", 1, newImages.size)
        assertEquals("The URL should match the mocked response", "https://example.com/cat.jpg", newImages.first())
    }
}

