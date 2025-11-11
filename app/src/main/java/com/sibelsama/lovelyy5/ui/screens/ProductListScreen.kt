package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.ui.components.ProductCard
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel

@Composable
fun ProductListScreen(onProductClick: (Product) -> Unit, onCartClick: () -> Unit, cartViewModel: CartViewModel = viewModel()) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val sampleProducts = listOf(
        Product(1, "iPhone 13 mini", "iPhone 13 mini", 130000.0),
        Product(2, "Honor K50 Gaming", "Honor K50 Gaming", 250000.0),
        Product(3, "iPhone 13", "iPhone 13", 130000.0)
    )

    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(sampleProducts) { product ->
                ProductCard(
                    product = product,
                    onProductClick = onProductClick,
                    onAddToCartClick = { cartViewModel.addToCart(product) }
                )
            }
        }
        Button(onClick = onCartClick) {
            Text("Ir al carrito (${cartItems.size})")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    LovelyY5APPTheme {
        ProductListScreen(onProductClick = {}, onCartClick = {})
    }
}
