package com.sibelsama.lovelyy5.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.model.Product

@Composable
fun ProductCard(product: Product, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(product.name, style = MaterialTheme.typography.h6)
            Text(product.description)
            Text("$${product.price}")
            Button(
                onClick = onAddToCart, modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Agregar al carrito")
            }
        }
    }
}
