package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.R
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.viewmodel.AppViewModel
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

// This will be the reusable AppBar
@Composable
fun AppHeader(isHome: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
        Text(
            text = "Lovely Y5 - App",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        if (isHome) {
            Text(
                text = "Tecnología al alcance de tu bolsillo",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun CartScreen(onCheckoutClick: () -> Unit, appViewModel: AppViewModel) {
    val cartItems by appViewModel.cart.collectAsState()
    val total = cartItems.entries.sumOf { (product, quantity) -> product.price * quantity }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Carrito de Compras",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cartItems.entries.toList()) { (product, quantity) ->
                    CartItem(
                        product = product,
                        quantity = quantity,
                        onQuantityChange = { newQuantity ->
                            appViewModel.updateQuantity(product, newQuantity)
                        },
                        onRemove = { appViewModel.removeFromCart(product) }
                    )
                }
            }
        }
        Surface(shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Total:", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Text(text = "$${total.toInt()} CLP", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onCheckoutClick,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = cartItems.isNotEmpty()
                ) {
                    Text("Proceder al Pago")
                }
            }
        }
    }
}

@Composable
fun CartItem(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Placeholder
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = "$${product.price.toInt()} CLP", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.End) {
                 IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = { if (quantity > 1) onQuantityChange(quantity - 1) else onRemove() }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Remove, contentDescription = "Quitar uno")
                    }
                    Text(text = quantity.toString(), style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { onQuantityChange(quantity + 1) }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir uno")
                    }
                }
            }
        }
    }
}

// Dummy ViewModel for preview
open class PreviewAppViewModel : AppViewModel() {
    private val _cart = MutableStateFlow<Map<Product, Int>>(emptyMap())
    override val cart: StateFlow<Map<Product, Int>> = _cart

    init {
        val p1 = Product(1, "Zapatillas Deportivas", "desc", 45990.0)
        val p2 = Product(2, "Polera Básica", "desc", 12990.0)
        _cart.value = mapOf(p1 to 1, p2 to 2)
    }
}


@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    LovelyY5APPTheme {
        CartScreen(onCheckoutClick = {}, appViewModel = PreviewAppViewModel())
    }
}