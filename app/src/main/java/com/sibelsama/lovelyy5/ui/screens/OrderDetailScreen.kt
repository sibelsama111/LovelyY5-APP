package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.model.ShippingDetails
import com.sibelsama.lovelyy5.ui.components.AppHeader
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme

@Composable
fun OrderDetailScreen(order: Order, onBack: () -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Detalles del Pedido #${order.id}",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            ShippingDetailsCard(shippingDetails = order.shippingDetails)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text("Productos", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(order.items.entries.toList()) { (productId, quantity) ->
            ProductDetailRow(productId = productId, quantity = quantity)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            OrderSummary(order = order)
        }
    }
}

@Composable
fun ShippingDetailsCard(shippingDetails: ShippingDetails) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text("Datos de Envío", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text("RUT: ${shippingDetails.rut}")
            Text("Nombre: ${shippingDetails.names} ${shippingDetails.lastNames}")
            Text("Teléfono: ${shippingDetails.phone}")
            Text("Correo: ${shippingDetails.email}")
            Text("Dirección: ${shippingDetails.address}")
            Text("Región: ${shippingDetails.region}")
        }
    }
}

@Composable
fun ProductDetailRow(productId: Int, quantity: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Text(text = "Producto #$productId", modifier = Modifier.weight(1f))
        Text(text = "x$quantity", modifier = Modifier.padding(horizontal = 16.dp))
        // Precio no disponible aquí sin catálogo; mostrar placeholder
        Text(text = "Precio: -")
    }
}

@Composable
fun OrderSummary(order: Order) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Subtotal", style = MaterialTheme.typography.bodyLarge)
            Text("$${order.subtotal.toInt()} CLP", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tarifa de Envío", style = MaterialTheme.typography.bodyLarge)
            Text("$${order.shippingCost.toInt()} CLP", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text("$${order.total.toInt()} CLP", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderDetailScreenPreview() {
    val dummyOrder = Order(
        id = "00001",
        shippingDetails = ShippingDetails("12.345.678-9", "Gino", "Sama", "+56912345678", "gino@sama.com", "Calle Falsa 123", "RM - Región Metropolitana"),
        items = mapOf(1 to 2, 2 to 1),
        subtotal = 510000.0,
        shippingCost = 5000.0,
        total = 515000.0
    )
    LovelyY5APPTheme {
        OrderDetailScreen(order = dummyOrder, onBack = {})
    }
}
