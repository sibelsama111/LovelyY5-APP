package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import com.sibelsama.lovelyy5.model.Product
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CartViewModelTest {

    private lateinit var cartViewModel: CartViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cartViewModel = CartViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addToCart should add new product`() = runTest {
        val product = Product(1, "Test Product", "Description", 100.0, "Tipo")

        cartViewModel.addToCart(product)

        val cartItems = cartViewModel.cartItems.first()
        assertEquals(1, cartItems.size)
        assertEquals(1, cartItems[product])
    }

    @Test
    fun `addToCart should increase quantity of existing product`() = runTest {
        val product = Product(1, "Test Product", "Description", 100.0, "Tipo")

        cartViewModel.addToCart(product)
        cartViewModel.addToCart(product)

        val cartItems = cartViewModel.cartItems.first()
        assertEquals(1, cartItems.size)
        assertEquals(2, cartItems[product])
    }

    @Test
    fun `removeFromCart should remove product completely`() = runTest {
        val product = Product(1, "Test Product", "Description", 100.0, "Tipo")
        cartViewModel.addToCart(product)

        cartViewModel.removeFromCart(product)

        val cartItems = cartViewModel.cartItems.first()
        assertEquals(0, cartItems.size)
    }

    @Test
    fun `updateQuantity should change product quantity`() = runTest {
        val product = Product(1, "Test Product", "Description", 100.0, "Tipo")
        cartViewModel.addToCart(product)

        cartViewModel.updateQuantity(product, 5)

        val cartItems = cartViewModel.cartItems.first()
        assertEquals(5, cartItems[product])
    }

    @Test
    fun `clearCart should empty the cart`() = runTest {
        val product1 = Product(1, "Test Product 1", "Desc 1", 100.0, "Tipo")
        val product2 = Product(2, "Test Product 2", "Desc 2", 200.0, "Tipo")
        cartViewModel.addToCart(product1)
        cartViewModel.addToCart(product2)

        cartViewModel.clearCart()

        val cartItems = cartViewModel.cartItems.first()
        assertEquals(0, cartItems.size)
    }
}

