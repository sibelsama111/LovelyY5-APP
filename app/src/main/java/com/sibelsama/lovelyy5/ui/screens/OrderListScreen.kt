package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.ShippingDetails
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme

@Composable
fun OrderListScreen(orders: List<Order>, onOrderClick: (String) -> Unit, onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header con botón de retroceso
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mis Pedidos",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (orders.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No has realizado ningún pedido.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders) { order ->
                    OrderItem(order = order, onClick = { onOrderClick(order.id) })
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Pedido #${order.id}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Este pedido fue de ${order.items.size} productos.", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Total: $${order.total.toInt()} CLP", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderListScreenPreview() {
    val dummyOrders = listOf(
        Order(
            id = "00001",
            shippingDetails = ShippingDetails("12.345.678-9", "Gino", "Sama", "+56912345678", "gino@sama.com", "Calle Falsa 123", "RM - Región Metropolitana"),
            // items: Map<productId, quantity>
            items = mapOf(1 to 3),
            subtotal = 390000.0,
            shippingCost = 5000.0,
            total = 395000.0
        )
    )
    LovelyY5APPTheme {
        OrderListScreen(orders = dummyOrders, onOrderClick = {}, onBackClick = {})
    }
}
