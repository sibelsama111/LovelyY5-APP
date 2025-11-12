package com.sibelsama.lovelyy5.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.model.ShippingDetails
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme

// Basic validation functions
private fun validateRut(rut: String): Boolean {
    val regex = Regex("""^([0-9]{1,2}\.?[0-9]{3}\.?[0-9]{3}-?[0-9kK])$""")
    return regex.matches(rut)
}

private fun validatePhone(phone: String): Boolean {
    return phone.startsWith("+56") && phone.length == 12 && phone.substring(3).all { it.isDigit() }
}

private fun validateEmail(email: String): Boolean {
    return "@" in email && "." in email
}

@SuppressLint("MissingPermission")
@Composable
fun CheckoutFormScreen(
    shippingDetails: ShippingDetails = ShippingDetails(
        rut = "20.XXX.XXX-K",
        names = "Laurita Jimenez",
        lastNames = "",
        phone = "+56 9 9999 9999",
        email = "laurita.jiji@hotmail.com",
        address = "Calle verde #35, comuna segura",
        region = "XV - Región de Arica y Parinacota"
    ),
    products: List<Pair<Product, Int>> = listOf(
        Product(1, "iPad Air", "Rose Gold - 128 GB", 213000.0) to 3,
        Product(2, "iPhone 13 mini", "Space grey - 1TB GB", 213000.0) to 1
    ),
    shippingFee: Double = 5000.0,
    onConfirm: () -> Unit = {}
) {
    val subtotal = products.sumOf { (product, qty) -> product.price * qty }
    val total = subtotal + shippingFee

    var showConfirmation by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* back action could be provided via callback */ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Checkout",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // ...existing code...
                Spacer(modifier = Modifier.height(24.dp))
                // Mostrar resumen de shipping y totales para usar las variables y eliminar advertencias
                Text("Envío a: ${shippingDetails.names} - ${shippingDetails.address}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Total a pagar: $${total.toInt()} CLP", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        showConfirmation = true
                        onConfirm()
                        // Haptic feedback en lugar de vibrator
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Confirmar compra", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (showConfirmation) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(32.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "¡Gracias por confiar en nosotros!\nPuedes ver tus pedidos desde",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "aquí",
                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.clickable {
                                    showConfirmation = false
                                    // Navegar a pantalla de pedidos
                                    onConfirm()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutFormScreenPreview() {
    LovelyY5APPTheme {
        CheckoutFormScreen(onConfirm = {})
    }
}
