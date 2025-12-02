package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.ProductItem
import com.sibelsama.lovelyy5.ui.components.ProductImage
import com.sibelsama.lovelyy5.ui.viewmodels.ProductViewModel

@Composable
fun OrderDetailScreen(
    order: Order,
    productViewModel: ProductViewModel,
    onProductClick: (ProductItem) -> Unit,
    onBackClick: () -> Unit
) {
    val products by productViewModel.products.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Detalle del Pedido",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // Order Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pedido #${order.id}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Shipping Details
                Text(
                    text = "Información de Envío",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                DetailRow("Nombre", "${order.shippingDetails.names} ${order.shippingDetails.lastNames}")
                DetailRow("RUT", order.shippingDetails.rut)
                DetailRow("Teléfono", order.shippingDetails.phone)
                DetailRow("Email", order.shippingDetails.email)
                DetailRow("Dirección", order.shippingDetails.address)
                DetailRow("Comuna", order.shippingDetails.comuna)
                DetailRow("Región", order.shippingDetails.region)

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // Price Summary
                Text(
                    text = "Resumen de Compra",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                DetailRow("Subtotal", "$${order.subtotal.toInt()} CLP")
                DetailRow("Costo de envío", "$${order.shippingCost.toInt()} CLP")
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow(
                    "Total",
                    "$${order.total.toInt()} CLP",
                    labelStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    valueStyle = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        // Products List
        Text(
            text = "Productos (${order.items.size})",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        order.items.forEach { (productId, quantity) ->
            val product = products.find { it.id == productId }
            if (product != null) {
                OrderProductItem(
                    product = product,
                    quantity = quantity,
                    onClick = { onProductClick(product) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    labelStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    valueStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = labelStyle,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = valueStyle,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}

@Composable
private fun OrderProductItem(
    product: ProductItem,
    quantity: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image
            ProductImage(
                imagePath = product.imagenes.firstOrNull(),
                contentDescription = product.nombre,
                modifier = Modifier.size(80.dp)
            )

            // Product Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cantidad: $quantity",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${product.precioActual.toInt()} CLP c/u",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "Subtotal: $${(product.precioActual * quantity).toInt()} CLP",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

