package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.viewmodel.AppViewModel

@Composable
fun CartScreen(onCheckoutClick: () -> Unit, appViewModel: AppViewModel = viewModel()) {
    LazyColumn {
        items(appViewModel.cart.size) { idx ->
            val product = appViewModel.cart[idx]
            Text("${product.name}: $${product.price}")
        }
    }
    Button(onClick = onCheckoutClick) {
        Text("Finalizar compra")
    }
}
