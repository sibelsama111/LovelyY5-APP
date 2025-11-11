package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import android.os.VibrationEffect
import android.os.Vibrator
import com.sibelsama.lovelyy5.model.ShippingDetails

@Composable
fun ShippingFormScreen(
    onSubmit: (ShippingDetails) -> Unit,
    onCancel: () -> Unit
) {
    var rut by remember { mutableStateOf("") }
    var names by remember { mutableStateOf("") }
    var lastNames by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val regions = listOf(
        "I - Región de Tarapacá", "II - Región de Antofagasta", "III - Región de Atacama",
        "IV - Región de Coquimbo", "V - Región de Valparaíso", "VI - Región de O'Higgins",
        "VII - Región del Maule", "VIII - Región del Biobío", "IX - Región de La Araucanía",
        "X - Región de Los Lagos", "XI - Región de Aysén", "XII - Región de Magallanes",
        "RM - Región Metropolitana", "XIV - Región de Los Ríos", "XV - Región de Arica y Parinacota",
        "XVI - Región de Ñuble"
    )
    var selectedRegion by remember { mutableStateOf("No seleccionada") }
    var expanded by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    fun validateRut(rut: String): Boolean {
        val regex = Regex("^\d{1,2}\.\d{3}\.\d{3}-[\dkK]")
        if (!regex.matches(rut)) return false
        // Validación de dígito verificador
        val parts = rut.split("-")
        if (parts.size != 2) return false
        val number = parts[0].replace(".", "")
        val dv = parts[1].lowercase()
        var m = 0
        var s = 1
        for (c in number.reversed()) {
            s = (s + c.digitToInt() * (9 - m % 6)) % 11
            m++
        }
        val expectedDv = if (s != 0) (s + 47).toChar().lowercaseChar().toString() else "k"
        return dv == expectedDv
    }

    fun validatePhone(phone: String): Boolean {
        val regex = Regex("^\+56\d{9}")
        return regex.matches(phone)
    }

    fun validateEmail(email: String): Boolean {
        val parts = email.split("@")
        return parts.size == 2 && "." in parts[1]
    }

    fun validateForm(): Boolean {
        val errors = mutableListOf<String>()
        if (!validateRut(rut)) {
            errors.add("- RUT: formato XX.XXX.XXX-X y dígito verificador válido.")
        }
        if (!validatePhone(phone)) {
            errors.add("- Teléfono: formato +56999999999.")
        }
        if (!validateEmail(email)) {
            errors.add("- Correo: debe contener '@' y un dominio válido.")
        }
        if (selectedRegion == "No seleccionada") {
            errors.add("- Región: debes seleccionar una región.")
        }
        errorMessage = if (errors.isNotEmpty()) {
            "Corrige los siguientes campos:\n" + errors.joinToString("\n")
        } else ""
        return errors.isEmpty()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Datos de Envío", fontWeight = FontWeight.Bold) })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            var editing by remember { mutableStateOf(false) }
            if (!editing) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = rut,
                        onValueChange = {},
                        label = { Text("RUT (XX.XXX.XXX-X)") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(value = names, onValueChange = {}, label = { Text("Nombres") }, readOnly = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = lastNames, onValueChange = {}, label = { Text("Apellidos") }, readOnly = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {},
                        label = { Text("Teléfono (+56999999999)") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = {},
                        label = { Text("Correo Electrónico") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = {},
                        label = { Text("Dirección (Ej: Calle #99, Comuna Asombrosa)") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = selectedRegion,
                        onValueChange = {},
                        label = { Text("Región") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = {
                            val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                            vibrator?.let {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    it.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                                } else {
                                    @Suppress("DEPRECATION")
                                    it.vibrate(50)
                                }
                            }
                            editing = true
                        }, modifier = Modifier.weight(1f)) {
                            Text("Editar")
                        }
                        Button(onClick = {
                            val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                            vibrator?.let {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    it.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                                } else {
                                    @Suppress("DEPRECATION")
                                    it.vibrate(50)
                                }
                            }
                            onCancel()
                        }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                            Text("Eliminar")
                        }
                        Button(
                            onClick = {
                                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                                vibrator?.let {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        it.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
                                    } else {
                                        @Suppress("DEPRECATION")
                                        it.vibrate(2000)
                                    }
                                }
                                if (validateForm()) {
                                    onSubmit(
                                        ShippingDetails(
                                            rut = rut,
                                            names = names,
                                            lastNames = lastNames,
                                            phone = phone,
                                            email = email,
                                            address = address,
                                            region = selectedRegion
                                        )
                                    )
                                } else {
                                    showError = true
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = rut.isNotBlank() && names.isNotBlank() && lastNames.isNotBlank() && phone.isNotBlank() && email.isNotBlank() && address.isNotBlank() && selectedRegion != "No seleccionada"
                        ) {
                            Text("Confirmar")
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = rut,
                        onValueChange = { rut = it },
                        label = { Text("RUT (XX.XXX.XXX-X)") },
                        isError = showError && !validateRut(rut),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(value = names, onValueChange = { names = it }, label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = lastNames, onValueChange = { lastNames = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Teléfono (+56999999999)") },
                        isError = showError && !validatePhone(phone),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo Electrónico") },
                        isError = showError && !validateEmail(email),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Dirección (Ej: Calle #99, Comuna Asombrosa)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box {
                        OutlinedTextField(
                            value = selectedRegion,
                            onValueChange = {},
                            label = { Text("Región") },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.fillMaxWidth().clickable { expanded = true }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            regions.forEach { region ->
                                DropdownMenuItem(text = { Text(region) }, onClick = { selectedRegion = region; expanded = false })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = {
                            val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                            vibrator?.let {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    it.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                                } else {
                                    @Suppress("DEPRECATION")
                                    it.vibrate(50)
                                }
                            }
                            editing = false
                        }, modifier = Modifier.weight(1f)) {
                            Text("Guardar cambios")
                        }
                        Button(onClick = {
                            val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
                            vibrator?.let {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    it.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.EFFECT_TICK))
                                } else {
                                    @Suppress("DEPRECATION")
                                    it.vibrate(50)
                                }
                            }
                            onCancel()
                        }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                            Text("Eliminar")
                        }
                    }
                }
            }
            if (showError) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            }
        }
    }
}
