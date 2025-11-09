package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.ui.components.ProductCard
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.viewmodel.AppViewModel

@Composable
fun ProductListScreen(onCartClick: () -> Unit, appViewModel: AppViewModel = viewModel()) {
    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(appViewModel.productList) { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { appViewModel.cart.add(product) }
                )
            }
        }
        Button(onClick = onCartClick) {
            Text("Ir al carrito (${appViewModel.cart.size})")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    LovelyY5APPTheme {
        val appViewModel = AppViewModel()
        appViewModel.productList.add(Product(1, "Product A", "Description A", 15.0))
        appViewModel.productList.add(Product(2, "Product B", "Description B", 25.0))
        ProductListScreen(onCartClick = {}, appViewModel = appViewModel)
    }
}
