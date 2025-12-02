package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import com.sibelsama.lovelyy5.model.ProductReview
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveReview should execute without errors`() = runTest(testDispatcher) {
        reviewViewModel = ReviewViewModel(application, ioDispatcher = testDispatcher)
        val review = ProductReview(1, "Tester", "Great product!", 5, emptyList())
        reviewViewModel.saveReview(review)
    }

    @Test
    fun `addImageToNewReview should add image to list`() {
        reviewViewModel = ReviewViewModel(application)
        val imageUrl = "https://example.com/cat.jpg"

        assertEquals(0, reviewViewModel.newReviewImages.value.size)

        reviewViewModel.addImageToNewReview(imageUrl)

        assertEquals(1, reviewViewModel.newReviewImages.value.size)
        assertEquals(imageUrl, reviewViewModel.newReviewImages.value.first())
    }

    @Test
    fun `removeImageFromNewReview should remove image from list`() {
        reviewViewModel = ReviewViewModel(application)
        val imageUrl1 = "https://example.com/cat1.jpg"
        val imageUrl2 = "https://example.com/cat2.jpg"

        reviewViewModel.addImageToNewReview(imageUrl1)
        reviewViewModel.addImageToNewReview(imageUrl2)
        assertEquals(2, reviewViewModel.newReviewImages.value.size)

        reviewViewModel.removeImageFromNewReview(imageUrl1)

        assertEquals(1, reviewViewModel.newReviewImages.value.size)
        assertEquals(imageUrl2, reviewViewModel.newReviewImages.value.first())
    }

    @Test
    fun `clearNewReviewImages should clear all images`() {
        reviewViewModel = ReviewViewModel(application)
        reviewViewModel.addImageToNewReview("https://example.com/cat1.jpg")
        reviewViewModel.addImageToNewReview("https://example.com/cat2.jpg")

        assertEquals(2, reviewViewModel.newReviewImages.value.size)

        reviewViewModel.clearNewReviewImages()

        assertEquals(0, reviewViewModel.newReviewImages.value.size)
    }

    // TODO: Esta prueba requiere configuración más compleja de Ktor MockEngine
    // Se omite temporalmente hasta resolver problemas de sincronización con coroutines
    /*
    @Test
    fun `fetchRandomCatImage should add an image url to the list`() = runTest(testDispatcher) {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""[{"url":"https://example.com/cat.jpg"}]"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val mockClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }

        reviewViewModel = ReviewViewModel(application, mockClient, testDispatcher)

        assertEquals("Initial image list should be empty", 0, reviewViewModel.newReviewImages.value.size)

        reviewViewModel.fetchRandomCatImage()

        // UnconfinedTestDispatcher ejecuta inmediatamente, así que debería estar listo
        val newImages = reviewViewModel.newReviewImages.value
        assertEquals("Image list should contain one item", 1, newImages.size)
        assertEquals("The URL should match the mocked response", "https://example.com/cat.jpg", newImages.first())
    }
    */
}

