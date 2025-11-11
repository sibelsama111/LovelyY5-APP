package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.model.ShippingDetails
import com.sibelsama.lovelyy5.ui.components.AppHeader
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme

// Basic validation functions
fun validateRut(rut: String): Boolean {
    val regex = Regex("^([0-9]{1,2}\\\.?[0-9]{3}\\\.?[0-9]{3}-?[0-9kK])\$")
    return regex.matches(rut)
}

fun validatePhone(phone: String): Boolean {
    return phone.startsWith("+56") && phone.length == 12 && phone.substring(3).all { it.isDigit() }
}

fun validateEmail(email: String): Boolean {
    return "@" in email && "." in email
}

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
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: back navigation */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
                Button(
                    onClick = {
                        showConfirmation = true
                        onConfirm()
                        // Vibración al confirmar
                        val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                        vibrator?.let {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                it.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                            } else {
                                @Suppress("DEPRECATION")
                                it.vibrate(200)
                            }
                        }
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
                                    onConfirm() // Puedes usar un callback para cambiar de pantalla
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
        CheckoutFormScreen()
    }
}
