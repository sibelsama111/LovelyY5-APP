package com.sibelsama.lovelyy5.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.viewmodels.AuthViewModel
import com.sibelsama.lovelyy5.model.AuthResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var identifier by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading.collectAsState()
    val authResult by authViewModel.authResult.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = false)

    // Navegar cuando el login sea exitoso
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onLoginSuccess()
        }
    }

    // Mostrar error si existe
    authResult?.let { result ->
        if (!result.isSuccess && result.errorMessage != null) {
            LaunchedEffect(result) {
                // El error se muestra en la UI, aquí podríamos agregar un SnackBar si queremos
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo y título
        Icon(
            Icons.Filled.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "LovelyY5",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Bienvenido de vuelta",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // RUT o Email
        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text("RUT o Email *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            placeholder = { Text("12345678-9 o email@ejemplo.cl") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contraseña
        OutlinedTextField(
            value = passwd,
            onValueChange = { passwd = it },
            label = { Text("Contraseña *") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar error
        authResult?.let { result ->
            if (!result.isSuccess && result.errorMessage != null) {
                Text(
                    text = result.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de login
        Button(
            onClick = {
                authViewModel.clearAuthResult()
                authViewModel.login(identifier, passwd)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && identifier.isNotBlank() && passwd.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Iniciar Sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace a registro
        Row {
            Text("¿No tienes cuenta? ")
            TextButton(
                onClick = onNavigateToRegister,
                enabled = !isLoading
            ) {
                Text("Regístrate aquí")
            }
        }
    }
}
