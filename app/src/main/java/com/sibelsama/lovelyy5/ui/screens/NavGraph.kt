package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel
import androidx.compose.ui.tooling.preview.Preview

// Instancia usada exclusivamente para previews (construida fuera de la composición)
private val previewCartVm = CartViewModel()

sealed class Screen {
    object Home : Screen()
    object Products : Screen()
    data class ProductDetail(val productId: Int) : Screen()
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

    when (val screen = currentScreen) {
        is Screen.Home -> HomeScreen(
            onProductClick = { product -> currentScreen = Screen.ProductDetail(product.id) },
            onCartClick = { currentScreen = Screen.Cart },
            onOrdersClick = { /* orders removed in simplified app */ },
            cartViewModel = cartVm
        )
        is Screen.Products -> ProductListScreen(
            onProductClick = { product -> currentScreen = Screen.ProductDetail(product.id) },
            onCartClick = { currentScreen = Screen.Cart },
            cartViewModel = cartVm,
            onBackClick = { currentScreen = Screen.Home }
        )
        is Screen.ProductDetail -> {
            // Use a small local sample catalog for previews/navigation
            val sampleProducts = listOf(
                com.sibelsama.lovelyy5.model.Product(1, "iPhone 13 mini", "iPhone 13 mini", 130000.0),
                com.sibelsama.lovelyy5.model.Product(2, "iPhone 13", "iPhone 13", 180000.0)
            )
            val product = sampleProducts.find { it.id == screen.productId }
            if (product != null) {
                ProductDetailScreen(
                    product = product,
                    reviewViewModel = reviewVm,
                    onAddToCart = { cartVm.addToCart(product) },
                    onBackClick = { currentScreen = Screen.Home }
                )
            } else {
                Text("Producto no encontrado")
            }
        }
        is Screen.Cart -> CartScreen(
            onConfirmProducts = { cartVm.clearCart() },
            onClearCart = { cartVm.clearCart() },
            onBackClick = { currentScreen = Screen.Home },
            cartViewModel = cartVm
        )
    }
}
@Preview(showBackground = true)
@Composable
fun NavGraphPreview() {
    LovelyY5APPTheme {
        NavGraph(cartViewModel = previewCartVm)
    }
}
