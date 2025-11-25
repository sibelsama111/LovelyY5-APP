package com.sibelsama.lovelyy5.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel
import com.sibelsama.lovelyy5.repository.ProductRepository
import androidx.compose.ui.tooling.preview.Preview


sealed class Screen {
    object Home : Screen()
    data class Products(val searchQuery: String? = null) : Screen()
    data class ProductDetail(val productId: Int) : Screen()
    object Orders : Screen()
    data class OrderDetail(val orderId: String) : Screen()
    data class Checkout(val items: Map<com.sibelsama.lovelyy5.model.Product, Int>) : Screen()
    object Cart : Screen()
}

@Composable
fun NavGraph(
    cartViewModel: CartViewModel? = null,
    reviewViewModel: ReviewViewModel? = null
) {
    // Crear viewModels dentro de la composición si no se inyectaron
    val cartVm: CartViewModel = cartViewModel ?: viewModel()
    val reviewVm: ReviewViewModel = reviewViewModel ?: viewModel()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    // Manejo del botón back físico del dispositivo
    BackHandler(enabled = currentScreen != Screen.Home) {
        when (currentScreen) {
            is Screen.Products -> currentScreen = Screen.Home
            is Screen.ProductDetail -> currentScreen = Screen.Home
            is Screen.Cart -> currentScreen = Screen.Home
            is Screen.Checkout -> currentScreen = Screen.Cart
            is Screen.Orders -> currentScreen = Screen.Home
            is Screen.OrderDetail -> currentScreen = Screen.Orders
            else -> currentScreen = Screen.Home
        }
    }

    when (val screen = currentScreen) {
        is Screen.Home -> HomeScreen(
            onProductClick = { product -> currentScreen = Screen.ProductDetail(product.id) },
            onCartClick = { currentScreen = Screen.Cart },
            onOrdersClick = { currentScreen = Screen.Orders },
            onSeeAllProducts = { currentScreen = Screen.Products(null) },
            onCategoryClick = { category -> currentScreen = Screen.Products(category) },
            cartViewModel = cartVm
        )
        is Screen.Products -> ProductListScreen(
            onProductClick = { product -> currentScreen = Screen.ProductDetail(product.id) },
            onCartClick = { currentScreen = Screen.Cart },
            onBack = { currentScreen = Screen.Home },
            initialQuery = screen.searchQuery,
            cartViewModel = cartVm
        )
        is Screen.ProductDetail -> {
            val context = LocalContext.current
            val repo = remember { ProductRepository(context) }
            val product = repo.getProductById(screen.productId)
            if (product != null) {
                try {
                    ProductDetailScreen(
                        product = product,
                        reviewViewModel = reviewVm,
                        onAddToCart = { cartVm.addToCart(product) },
                        onBack = { currentScreen = Screen.Home }
                    )
                } catch (e: Exception) {
                    ErrorMessage(
                        message = "Error al cargar detalles del producto: ${e.message}",
                        onBack = { currentScreen = Screen.Home }
                    )
                }
            } else {
                ErrorMessage(
                    message = "Producto no encontrado",
                    onBack = { currentScreen = Screen.Home }
                )
            }
        }
        is Screen.Cart -> {
            try {
                val cartItems by cartVm.cartItems.collectAsState()
                CartScreen(
                    onConfirmProducts = { currentScreen = Screen.Checkout(cartItems) },
                    onClearCart = { cartVm.clearCart() },
                    onBack = { currentScreen = Screen.Home },
                    cartViewModel = cartVm
                )
            } catch (e: Exception) {
                ErrorMessage(
                    message = "Error al cargar el carrito: ${e.message}",
                    onBack = { currentScreen = Screen.Home }
                )
            }
        }
        is Screen.Checkout -> {
            val orderVm: com.sibelsama.lovelyy5.ui.viewmodels.OrderViewModel = viewModel()
            CheckoutFormScreen(
                products = screen.items.map { it.key to it.value },
                onConfirm = {
                    // create order from cart items; construct ShippingDetails minimal placeholder for now
                    orderVm.createOrder(screen.items.map { it.key to it.value }.toMap(), com.sibelsama.lovelyy5.model.ShippingDetails("","","","","","",""))
                    cartVm.clearCart()
                    currentScreen = Screen.Orders
                },
                onBack = { currentScreen = Screen.Cart }
            )
        }
        is Screen.Orders -> {
            val orderVm: com.sibelsama.lovelyy5.ui.viewmodels.OrderViewModel = viewModel()
            val orders by orderVm.orders.collectAsState()
            OrderListScreen(
                orders = orders,
                onOrderClick = { orderId -> currentScreen = Screen.OrderDetail(orderId) },
                onSeeProducts = { currentScreen = Screen.Products(null) },
                onBack = { currentScreen = Screen.Home }
            )
        }
        is Screen.OrderDetail -> {
            val orderVm: com.sibelsama.lovelyy5.ui.viewmodels.OrderViewModel = viewModel()
            val orders by orderVm.orders.collectAsState()
            val order = orders.find { it.id == screen.orderId }
            if (order != null) {
                OrderDetailScreen(
                    order = order,
                    onBack = { currentScreen = Screen.Orders }
                )
            } else {
                Text("Pedido no encontrado")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavGraphPreview() {
    LovelyY5APPTheme {
        NavGraph(cartViewModel = previewCartVm)
    }
}
