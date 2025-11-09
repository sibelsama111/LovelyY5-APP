package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme

@Composable
fun CheckoutFormScreen(onSubmit: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    val isFormValid = nombre.isNotBlank() && direccion.isNotBlank() && email.contains("@")

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección de envío") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email de contacto") },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMsg.isNotEmpty()) {
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = {
                if (isFormValid) {
                    onSubmit()
                } else {
                    errorMsg = "Por favor, completa todos los campos correctamente."
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Enviar pedido")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutFormScreenPreview() {
    LovelyY5APPTheme {
        CheckoutFormScreen(onSubmit = {})
    }
}
