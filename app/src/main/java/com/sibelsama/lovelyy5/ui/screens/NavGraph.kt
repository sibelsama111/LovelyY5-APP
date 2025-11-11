package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.model.ShippingDetails
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.viewmodel.AppViewModel

sealed class Screen {
    object Home : Screen()
    object Products : Screen()
    object Cart : Screen()
    object Checkout : Screen()
    object OrderList : Screen()
    data class OrderDetail(val orderId: String) : Screen()
}

@Composable
fun NavGraph(appViewModel: AppViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val orders by appViewModel.orders.collectAsState()

    when (val screen = currentScreen) {
        is Screen.Home -> HomeScreen(
            onProductClick = { currentScreen = Screen.Products },
            onCartClick = { currentScreen = Screen.Cart },
            onOrdersClick = { currentScreen = Screen.OrderList }
        )
        is Screen.Products -> ProductListScreen(
            onCartClick = { currentScreen = Screen.Cart },
            appViewModel = appViewModel
        )
        is Screen.Cart -> CartScreen(
            onCheckoutClick = { currentScreen = Screen.Checkout },
            appViewModel = appViewModel
        )
        is Screen.Checkout -> CheckoutFormScreen(
            onSubmit = { shippingDetails ->
                appViewModel.createOrder(shippingDetails)
                currentScreen = Screen.OrderList
            }
        )
        is Screen.OrderList -> OrderListScreen(
            orders = orders,
            onOrderClick = { orderId ->
                currentScreen = Screen.OrderDetail(orderId)
            }
        )
        is Screen.OrderDetail -> {
            val order = appViewModel.getOrderById(screen.orderId)
            if (order != null) {
                OrderDetailScreen(order = order)
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
        NavGraph()
    }
}
