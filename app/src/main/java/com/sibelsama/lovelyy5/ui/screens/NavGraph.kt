package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sibelsama.lovelyy5.ui.dopamina.DopaminaScreen
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.OrderViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ProductViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel

@Composable
fun NavGraph(
    cartViewModel: CartViewModel = viewModel(),
    reviewViewModel: ReviewViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        floatingActionButton = {
            if (currentRoute != "cart" && currentRoute != "orders" && currentRoute != "dopamina") {
                FloatingActionButton(onClick = { navController.navigate("dopamina") }) {
                    Icon(Icons.Default.Pets, contentDescription = "Dopamina")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    onProductClick = { product -> navController.navigate("product/${product.id}") },
                    onCartClick = { navController.navigate("cart") },
                    onOrdersClick = { navController.navigate("orders") },
                    onCategoryClick = { category -> navController.navigate("products/$category") },
                    onSeeAllProducts = { navController.navigate("products") },
                    onSeeAllCategories = { navController.navigate("products") },
                    cartViewModel = cartViewModel
                )
            }
            composable("products") {
                ProductListScreen(
                    onProductClick = { product -> navController.navigate("product/${product.id}") },
                    onCartClick = { navController.navigate("cart") },
                    cartViewModel = cartViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("products/{category}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")
                ProductListScreen(
                    onProductClick = { product -> navController.navigate("product/${product.id}") },
                    onCartClick = { navController.navigate("cart") },
                    cartViewModel = cartViewModel,
                    onBackClick = { navController.popBackStack() },
                    initialCategory = category
                )
            }
            composable("product/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                val productItem = productViewModel.products.collectAsState().value.find { it.id == productId }
                val product = productItem?.toProduct()
                if (product != null) {
                    ProductDetailScreen(
                        product,
                        productItem,
                        reviewViewModel,
                        { cartViewModel.addToCart(product) },
                        { navController.popBackStack() })
                } else {
                    Text("Producto no encontrado")
                }
            }
            composable("cart") {
                CartScreen(
                    onClearCart = { cartViewModel.clearCart() },
                    onBackClick = { navController.popBackStack() },
                    onPurchaseCompleted = { navController.navigate("orders") },
                    cartViewModel = cartViewModel
                )
            }
            composable("orders") {
                val orders by orderViewModel.orders.collectAsState()
                OrderListScreen(
                    orders = orders,
                    onOrderClick = { orderId -> navController.navigate("order/$orderId") },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("order/{orderId}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")
                val orders by orderViewModel.orders.collectAsState()
                val order = orders.find { it.id == orderId }
                if (order != null) {
                    OrderDetailScreen(
                        order = order,
                        productViewModel = productViewModel,
                        onProductClick = { product -> navController.navigate("product/${product.id}") },
                        onBackClick = { navController.popBackStack() }
                    )
                } else {
                    Text("Pedido no encontrado")
                }
            }
            composable("dopamina") {
                DopaminaScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavGraphPreview() {
    LovelyY5APPTheme {
        NavGraph()
    }
}

