package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel
import com.sibelsama.lovelyy5.model.Order
import androidx.compose.ui.tooling.preview.Preview

// Instancia usada exclusivamente para previews (construida fuera de la composición)
private val previewCartVm = CartViewModel()

sealed class Screen {
    object Home : Screen()
    data class Products(val category: String? = null) : Screen()
    data class ProductDetail(val productId: Int) : Screen()
    object Cart : Screen()
    object Orders : Screen()
    data class OrderDetail(val order: Order) : Screen()
}

@Composable
fun NavGraph(
    cartViewModel: CartViewModel? = null,
    reviewViewModel: ReviewViewModel? = null
) {
    // Crear viewModels dentro de la composición si no se inyectaron
    val cartVm: CartViewModel = cartViewModel ?: viewModel()
    val reviewVm: ReviewViewModel = reviewViewModel ?: viewModel()
    val productVm: com.sibelsama.lovelyy5.ui.viewmodels.ProductViewModel = viewModel()
    val orderVm: com.sibelsama.lovelyy5.ui.viewmodels.OrderViewModel = viewModel()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    when (val screen = currentScreen) {
        is Screen.Home -> HomeScreen(
            onProductClick = { product -> currentScreen = Screen.ProductDetail(product.id) },
            onCartClick = { currentScreen = Screen.Cart },
            onOrdersClick = { currentScreen = Screen.Orders },
            onCategoryClick = { category -> currentScreen = Screen.Products(category) },
            onSeeAllProducts = { currentScreen = Screen.Products(null) },
            onSeeAllCategories = { currentScreen = Screen.Products(null) },
            cartViewModel = cartVm
        )
        is Screen.Products -> {
            val category = screen.category
            ProductListScreen(
                onProductClick = { product -> currentScreen = Screen.ProductDetail(product.id) },
                onCartClick = { currentScreen = Screen.Cart },
                cartViewModel = cartVm,
                onBackClick = { currentScreen = Screen.Home },
                initialCategory = category
            )
        }
        is Screen.ProductDetail -> {
            // Buscar producto en el catálogo cargado desde JSON
            val productItem = productVm.products.collectAsState().value.find { it.id == screen.productId }
            val product = productItem?.toProduct()
            if (product != null) {
                // Pasar argumentos posicionalmente para evitar discrepancias en nombres
                ProductDetailScreen(product, productItem, reviewVm, { cartVm.addToCart(product) }, { currentScreen = Screen.Home })
            } else {
                Text("Producto no encontrado")
            }
        }
        is Screen.Cart -> CartScreen(
            onClearCart = { cartVm.clearCart() },
            onBackClick = { currentScreen = Screen.Home },
            onPurchaseCompleted = { currentScreen = Screen.Orders },
            cartViewModel = cartVm
        )
        is Screen.Orders -> {
            val orders by orderVm.orders.collectAsState()
            OrderListScreen(orders = orders, onOrderClick = { orderId ->
                val ord = orderVm.orders.value.find { it.id == orderId }
                if (ord != null) currentScreen = Screen.OrderDetail(ord)
            }, onBackClick = { currentScreen = Screen.Home })
        }
        is Screen.OrderDetail -> {
            OrderDetailScreen(order = screen.order, onBackClick = { currentScreen = Screen.Orders })
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
