package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel
import com.sibelsama.lovelyy5.repository.ProductRepository
import androidx.compose.ui.tooling.preview.Preview

// Instancia usada exclusivamente para previews (construida fuera de la composición)
private val previewCartVm = CartViewModel()

sealed class Screen {
    object Home : Screen()
    data class Products(val searchQuery: String? = null) : Screen()
    data class ProductDetail(val productId: Int) : Screen()
    object Orders : Screen()
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
                ProductDetailScreen(product = product, reviewViewModel = reviewVm, onAddToCart = { cartVm.addToCart(product) })
            } else {
                Text("Producto no encontrado")
            }
        }
        is Screen.Cart -> {
            val cartItems by cartVm.cartItems.collectAsState()
            CartScreen(
                onConfirmProducts = { currentScreen = Screen.Checkout(cartItems) },
                onClearCart = { cartVm.clearCart() },
                cartViewModel = cartVm
            )
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
                }
            )
        }
        is Screen.Orders -> {
            val orderVm: com.sibelsama.lovelyy5.ui.viewmodels.OrderViewModel = viewModel()
            val orders by orderVm.orders.collectAsState()
            OrderListScreen(orders = orders, onOrderClick = { orderId -> /* could show detail */ }, onSeeProducts = { currentScreen = Screen.Products(null) })
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
