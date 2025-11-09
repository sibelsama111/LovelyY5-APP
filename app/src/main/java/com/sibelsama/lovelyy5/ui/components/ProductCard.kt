package com.sibelsama.lovelyy5.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme

@Composable
fun ProductCard(product: Product, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleLarge)
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

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    LovelyY5APPTheme {
        val sampleProduct = Product(1, "Sample Product", "This is a sample product description.", 29.99)
        ProductCard(product = sampleProduct, onAddToCart = {})
    }
}
