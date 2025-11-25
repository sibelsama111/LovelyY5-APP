package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.model.ShippingDetails
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.R
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider

@Composable
fun OrderDetailScreen(order: Order, onBackClick: () -> Unit = {}) {
    val sampleProducts = listOf(
        Product(1, "iPad Air", "Rose Gold - 128 GB", 213000.0),
        Product(2, "iPhone 13 mini", "Space grey - 1TB GB", 250000.0),
        Product(3, "iPhone 13", "128 GB", 180000.0)
    )

    fun findProductById(id: Int): Product? = sampleProducts.find { it.id == id }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3F8)),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            // Header con botón de retroceso
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pedido ID #${order.id}",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Datos de envío en estilo más compacto
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Datos de Envío:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                Text(order.shippingDetails.rut, style = MaterialTheme.typography.bodySmall)
                Text("${order.shippingDetails.names} ${order.shippingDetails.lastNames}", style = MaterialTheme.typography.bodySmall)
                Text(order.shippingDetails.phone, style = MaterialTheme.typography.bodySmall)
                Text(order.shippingDetails.email, style = MaterialTheme.typography.bodySmall)
                Text(order.shippingDetails.address, style = MaterialTheme.typography.bodySmall)
                Text(order.shippingDetails.region, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Productos seleccionados:", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(order.items.entries.toList()) { (productId, quantity) ->
            val product = findProductById(productId)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    // placeholder image
                    Box(modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF0E8F5)), contentAlignment = Alignment.Center) {
                        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "img", modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product?.name ?: "Producto #$productId", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                        Text(text = product?.description ?: "-", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "x $quantity unidades", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        val unitPrice = product?.price ?: 0.0
                        Text(text = "$${unitPrice.toInt()} CLP", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "*precio unitario", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal:", style = MaterialTheme.typography.bodyMedium)
                Text("$${order.subtotal.toInt()} CLP", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tarifa de envío:", style = MaterialTheme.typography.bodyMedium)
                Text("$${order.shippingCost.toInt()} CLP", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total:", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Text("$${order.total.toInt()} CLP", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderDetailScreenPreview() {
    val dummyOrder = Order(
        id = "00001",
        shippingDetails = ShippingDetails("12.345.678-9", "Laurita", "Jimenez", "+56 9 9999 9999", "laurita.jiji@hotmail.com", "Calle verde #35, comuna segura", "XV - Región de Arica y Parinacota"),
        items = mapOf(1 to 3, 2 to 1),
        subtotal = 640000.0,
        shippingCost = 5000.0,
        total = 645000.0
    )
    LovelyY5APPTheme {
        OrderDetailScreen(order = dummyOrder)
    }
}
