package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.viewmodel.AppViewModel

@Composable
fun CartScreen(onCheckoutClick: () -> Unit, appViewModel: AppViewModel = viewModel()) {
    Column {
        LazyColumn {
            items(appViewModel.cart) { product ->
                Text("${product.name}: $${product.price}")
            }
        }
        Button(onClick = onCheckoutClick) {
            Text("Finalizar compra")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    LovelyY5APPTheme {
        val appViewModel = AppViewModel()
        appViewModel.cart.add(Product(1, "Product 1", "Description 1", 10.0))
        appViewModel.cart.add(Product(2, "Product 2", "Description 2", 20.0))
        CartScreen(onCheckoutClick = {}, appViewModel = appViewModel)
    }
}
