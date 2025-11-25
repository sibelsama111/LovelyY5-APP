package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.R
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel

@Composable
fun ProductListScreen(
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit,
    cartViewModel: CartViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val sampleProducts = listOf(
        Product(1, "iPhone 13 mini", "iPhone 13 mini", 130000.0),
        Product(2, "iPhone 13", "iPhone 13", 180000.0)
    )

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var sortOrder by remember { mutableStateOf(true) } // true: menor a mayor, false: mayor a menor

    val filteredProducts = sampleProducts
        .filter {
            it.name.contains(searchText.text, ignoreCase = true) ||
            it.description.contains(searchText.text, ignoreCase = true)
        }
        .sortedBy { if (sortOrder) it.price else -it.price }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F3F8))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                @Suppress("DEPRECATION")
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Productos", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            placeholder = { Text("Buscar por nombre o descripción") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ordenar por precio:", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { sortOrder = !sortOrder },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3D6F7))
            ) {
                Text(if (sortOrder) "Menor a mayor" else "Mayor a menor", color = Color(0xFFB36AE2))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (searchText.text.isNotBlank()) "\"${searchText.text}\"" else "Todos los productos",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredProducts) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onProductClick(product) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F3F8)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = product.name,
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(product.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                        Text("$${product.price.toInt()} CLP", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { cartViewModel.addToCart(product) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDE7F3))
                        ) {
                            Text("Añadir al carrito", color = Color(0xFFEA7CB3))
                        }
                    }
                }
            }
        }
        Button(
            onClick = onCartClick,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB36AE2))
        ) {
            Text("Ir al carrito (${cartItems.size})", color = Color.White)
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
