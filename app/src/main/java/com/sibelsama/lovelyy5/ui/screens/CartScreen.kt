package com.sibelsama.lovelyy5.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.viewmodels.RegionsViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ProductViewModel
import com.sibelsama.lovelyy5.model.Order
import androidx.compose.ui.platform.LocalContext
import android.os.VibrationEffect
import android.os.Vibrator
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.text.input.TextFieldValue
import com.sibelsama.lovelyy5.model.ShippingDetails
import com.sibelsama.lovelyy5.ui.viewmodels.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.sibelsama.lovelyy5.ui.components.ProductImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("MissingPermission")
fun CartScreen(
    onClearCart: () -> Unit,
    onBackClick: () -> Unit = {},
    onPurchaseCompleted: (Order) -> Unit = {},
    cartViewModel: CartViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val subtotal = cartItems.entries.sumOf { (product, quantity) -> product.price * quantity }

    val context = LocalContext.current
    val orderViewModel: OrderViewModel = viewModel()
    val productVm: ProductViewModel = viewModel()

    var showShippingForm by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    var rut by remember { mutableStateOf(TextFieldValue("")) }
    var names by remember { mutableStateOf(TextFieldValue("")) }
    var lastNames by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var region by remember { mutableStateOf(TextFieldValue("")) }
    var comuna by remember { mutableStateOf(TextFieldValue("")) }

    val regionsVm: RegionsViewModel = viewModel()
    val regionsList by regionsVm.regions.collectAsState()
    val loadedRegionsState = remember { mutableStateOf<List<com.sibelsama.lovelyy5.data.RegionEntry>>(regionsList) }
    LaunchedEffect(regionsList) {
        if (regionsList.isNotEmpty()) {
            loadedRegionsState.value = regionsList
        } else {
            try {
                val repo = com.sibelsama.lovelyy5.data.RegionsRepository(context)
                val loaded = repo.loadRegions()
                if (loaded.isNotEmpty()) loadedRegionsState.value = loaded
            } catch (e: Exception) {
                android.util.Log.e("CartScreen", "Error loading regions fallback: ${e.message}")
            }
        }
    }

    val regions = loadedRegionsState.value.map { it.name }

    var regionExpanded by remember { mutableStateOf(false) }
    var comunaExpanded by remember { mutableStateOf(false) }

    val shippingCost = 5000.0

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    @Suppress("DEPRECATION")
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Carrito",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío")
                }
            } else {
                Text("Productos seleccionados:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                cartItems.forEach { (product, qty) ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val prodItem = productVm.products.collectAsState().value.find { it.id == product.id }
                            ProductImage(imagePath = prodItem?.imagenes?.firstOrNull(), contentDescription = product.name, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("x $qty unidades", style = MaterialTheme.typography.bodyMedium)
                                    Text("$${product.price.toInt()} CLP", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                IconButton(onClick = { if (qty > 1) cartViewModel.updateQuantity(product, qty - 1) else cartViewModel.removeFromCart(product) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Quitar uno")
                                }
                                IconButton(onClick = { cartViewModel.updateQuantity(product, qty + 1) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Añadir uno")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal:", style = MaterialTheme.typography.bodyMedium)
                    Text("$${subtotal.toInt()} CLP", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tarifa de envío:", style = MaterialTheme.typography.bodyMedium)
                    Text("$${shippingCost.toInt()} CLP", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total:", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Text("$${(subtotal + shippingCost).toInt()} CLP", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        val vibrator = context.getSystemService(Vibrator::class.java)
                        if (vibrator != null) {
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                        }
                        onClearCart()
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Vaciar carrito", style = MaterialTheme.typography.titleMedium)
                }
                Button(
                    onClick = {
                        showShippingForm = true
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    enabled = cartItems.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Confirmar productos", style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showShippingForm) {
        AlertDialog(
            onDismissRequest = { showShippingForm = false },
            confirmButton = {
                val canConfirm = region.text.isNotBlank() && comuna.text.isNotBlank()
                TextButton(onClick = {
                    if (names.text.isBlank() || lastNames.text.isBlank() || phone.text.isBlank() || address.text.isBlank()) {
                        android.widget.Toast.makeText(context, "Por favor completa los datos de envío", android.widget.Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }

                    val rutRegex = Regex("^[0-9]{7,8}-[0-9Kk]$")
                    if (!rut.text.matches(rutRegex)) {
                        android.widget.Toast.makeText(context, "RUT inválido. Formato ejemplo: 12345678-K", android.widget.Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    val phoneRegex = Regex("^9[0-9]{8}$")
                    if (!phone.text.matches(phoneRegex)) {
                        android.widget.Toast.makeText(context, "Teléfono inválido. Debe comenzar con 9 y tener 9 dígitos por ejemplo 912345678", android.widget.Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
                    if (email.text.isNotBlank() && !email.text.matches(emailRegex)) {
                        android.widget.Toast.makeText(context, "Correo inválido", android.widget.Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    if (region.text.isBlank() || comuna.text.isBlank()) {
                        android.widget.Toast.makeText(context, "Selecciona región y comuna", android.widget.Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }

                    val shipping = ShippingDetails(
                        rut = rut.text,
                        names = names.text,
                        lastNames = lastNames.text,
                        phone = phone.text,
                        email = email.text,
                        address = address.text,
                        region = region.text,
                        comuna = comuna.text
                    )

                    val id = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

                    val snapshot = cartViewModel.cartItems.value
                    val itemsMap = snapshot.entries.associate { (product, qty) -> product.id to qty }
                    val total = subtotal + shippingCost

                    val order = Order(
                        id = id,
                        shippingDetails = shipping,
                        items = itemsMap,
                        subtotal = subtotal,
                        shippingCost = shippingCost,
                        total = total
                    )

                    orderViewModel.saveOrder(order)

                    @Suppress("DEPRECATION")
                    val vibrator = context.getSystemService(Vibrator::class.java)
                    vibrator?.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))

                    showShippingForm = false
                    showSuccessDialog = true

                    cartViewModel.clearCart()

                    onPurchaseCompleted(order)
                }, enabled = canConfirm) {
                    Text("Confirmar compra")
                }
            },
            dismissButton = {
                TextButton(onClick = { showShippingForm = false }) { Text("Cancelar") }
            },
            title = { Text("Datos de Envío") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("RUT") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = names, onValueChange = { names = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = lastNames, onValueChange = { lastNames = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(8.dp))
                    val regionSelected = region.text.isNotBlank()
                    val comunasOpciones = if (regionSelected) loadedRegionsState.value.find { it.name == region.text }?.comunas ?: emptyList() else emptyList()
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = region.text,
                            onValueChange = {},
                            label = { Text("Región") },
                            readOnly = true,
                            trailingIcon = { IconButton(onClick = { regionExpanded = !regionExpanded }) { Icon(Icons.Default.ArrowDropDown, contentDescription = null) } },
                            modifier = Modifier.fillMaxWidth().clickable { regionExpanded = !regionExpanded }
                        )
                        DropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                            if (regions.isEmpty()) {
                                DropdownMenuItem(text = { Text("Cargando regiones...") }, onClick = { })
                            } else {
                                regions.forEach { r ->
                                    DropdownMenuItem(text = { Text(r) }, onClick = {
                                        region = TextFieldValue(r)
                                        regionExpanded = false
                                        comuna = TextFieldValue("")
                                    })
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = comuna.text,
                            onValueChange = {},
                            label = { Text("Comuna") },
                            placeholder = { if (!regionSelected) Text("Selecciona una región primero") else null },
                            readOnly = true,
                            enabled = regionSelected,
                            trailingIcon = { IconButton(onClick = { if (regionSelected) comunaExpanded = !comunaExpanded else android.widget.Toast.makeText(context, "Selecciona una región primero", android.widget.Toast.LENGTH_SHORT).show() }) { Icon(Icons.Default.ArrowDropDown, contentDescription = null) } },
                            modifier = Modifier.fillMaxWidth().clickable(enabled = regionSelected) { if (regionSelected) comunaExpanded = !comunaExpanded else android.widget.Toast.makeText(context, "Selecciona una región primero", android.widget.Toast.LENGTH_SHORT).show() }
                        )
                        DropdownMenu(expanded = comunaExpanded, onDismissRequest = { comunaExpanded = false }) {
                            if (!regionSelected) {
                                DropdownMenuItem(text = { Text("Selecciona una región primero") }, onClick = { })
                            } else {
                                comunasOpciones.forEach { c ->
                                    DropdownMenuItem(text = { Text(c) }, onClick = {
                                        comuna = TextFieldValue(c)
                                        comunaExpanded = false
                                    })
                                }
                            }
                        }
                    }
                }
            }
        )

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Compra efectuada correctamente! <3") },
                confirmButton = {
                    TextButton(onClick = { showSuccessDialog = false }) { Text("OK") }
                }
            )
        }
    }
}
