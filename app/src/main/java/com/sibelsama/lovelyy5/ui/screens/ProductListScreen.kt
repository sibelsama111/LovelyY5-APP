package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.components.ProductCard
import com.sibelsama.lovelyy5.viewmodel.AppViewModel

@Composable
fun ProductListScreen(onCartClick: () -> Unit, appViewModel: AppViewModel = viewModel()) {
    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(appViewModel.productList.size) { idx ->
                ProductCard(
                    product = appViewModel.productList[idx],
                    onAddToCart = { appViewModel.cart.add(appViewModel.productList[idx]) }
                )
            }
        }
        Button(onClick = onCartClick) {
            Text("Ir al carrito (${appViewModel.cart.size})")
        }
    }
}
