package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.runtime.*
import com.sibelsama.lovelyy5.viewmodel.AppViewModel

@Composable
fun NavGraph(appViewModel: AppViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var currentScreen by remember { mutableStateOf("products") }
    when (currentScreen) {
        "products" -> ProductListScreen(onCartClick = { currentScreen = "cart" }, appViewModel = appViewModel)
        "cart" -> CartScreen(onCheckoutClick = { currentScreen = "checkout" }, appViewModel = appViewModel)
        "checkout" -> CheckoutFormScreen(onSubmit = { currentScreen = "products"; appViewModel.cart.clear() })
    }
}
