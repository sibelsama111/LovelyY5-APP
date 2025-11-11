package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutFormScreen(onSubmit: (ShippingDetails) -> Unit) {
    var rut by remember { mutableStateOf("") }
    var isRutError by remember { mutableStateOf(false) }
    var names by remember { mutableStateOf("") }
    var lastNames by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+56 ") }
    var isPhoneError by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("") }
    val regions = listOf(
        "I - Región de Tarapacá", "II - Región de Antofagasta", "III - Región de Atacama",
        "IV - Región de Coquimbo", "V - Región de Valparaíso", "VI - Región de O'Higgins",
        "VII - Región del Maule", "VIII - Región del Biobío", "IX - Región de La Araucanía",
        "X - Región de Los Lagos", "XI - Región de Aysén", "XII - Región de Magallanes",
        "RM - Región Metropolitana", "XIV - Región de Los Ríos", "XV - Región de Arica y Parinacota",
        "XVI - Región de Ñuble"
    )
    var selectedRegion by remember { mutableStateOf(regions[0]) }
    var expanded by remember { mutableStateOf(false) }

    fun validateFields(): Boolean {
        isRutError = !validateRut(rut)
        isPhoneError = !validatePhone(phone)
        isEmailError = !validateEmail(email)
        return !isRutError && !isPhoneError && !isEmailError && names.isNotBlank() && lastNames.isNotBlank() && address.isNotBlank()
    }

    Scaffold(
        topBar = { AppHeader() },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Formulario de Despacho", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("RUT (XX.XXX.XXX-X)") }, isError = isRutError, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = names, onValueChange = { names = it }, label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = lastNames, onValueChange = { lastNames = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono (+56 X XXXX XXXX)") }, isError = isPhoneError, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, isError = isEmailError, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección (calle #25, comuna)") }, modifier = Modifier.fillMaxWidth())

                Box {
                    OutlinedTextField(
                        value = selectedRegion,
                        onValueChange = { },
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
                Button(
                    onClick = {
                        if (validateFields()) {
                            val shippingDetails = ShippingDetails(
                                name = names,
                                address = address,
                                city = selectedRegion, // Assuming region is the city for simplicity
                                postalCode = "", // TODO: Add postal code field
                                email = email,
                                phone = phone
                            )
                            onSubmit(shippingDetails)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Confirmar Compra")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CheckoutFormScreenPreview() {
    LovelyY5APPTheme {
        CheckoutFormScreen(onSubmit = {})
    }
}
