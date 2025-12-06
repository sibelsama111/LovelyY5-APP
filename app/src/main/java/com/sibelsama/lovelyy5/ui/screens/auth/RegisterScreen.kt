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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.viewmodels.AuthViewModel
import com.sibelsama.lovelyy5.model.Direccion
import com.sibelsama.lovelyy5.data.RegionsData
import com.sibelsama.lovelyy5.ui.components.RegionDropdownSelector
import com.sibelsama.lovelyy5.ui.components.ComunaDropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var rut by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    // Campos de dirección
    var calle by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }

    // Opciones para dropdowns
    val regionOptions = remember { RegionsData.getRegionNames() }
    val comunaOptions = remember(region) {
        if (region.isNotEmpty()) RegionsData.getComunasByRegion(region) else emptyList()
    }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading.collectAsState()
    val authResult by authViewModel.authResult.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = false)

    // Navegar cuando el registro sea exitoso
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onRegisterSuccess()
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
            Icons.Filled.PersonAdd,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Únete a LovelyY5",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // RUT
        OutlinedTextField(
            value = rut,
            onValueChange = { rut = it },
            label = { Text("RUT *") },
            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            placeholder = { Text("12345678-9") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nombres
        OutlinedTextField(
            value = nombres,
            onValueChange = { nombres = it },
            label = { Text("Nombres *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            placeholder = { Text("Jacqueline Sibel") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Apellidos
        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            placeholder = { Text("Torti Parraguez") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email *") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Teléfono (obligatorio)
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono 9 dígitos *") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            placeholder = { Text("989598097") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección Dirección
        Text(
            text = "Dirección de Entrega",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Calle
        OutlinedTextField(
            value = calle,
            onValueChange = { calle = it },
            label = { Text("Calle *") },
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            placeholder = { Text("Ossandon") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Número
        OutlinedTextField(
            value = numero,
            onValueChange = { numero = it },
            label = { Text("Número *") },
            leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            placeholder = { Text("401") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Región (Dropdown)
        RegionDropdownSelector(
            selectedRegion = region,
            onRegionSelected = { newRegion ->
                region = newRegion
                // Limpiar comuna cuando cambia la región
                comuna = ""
            },
            regions = regionOptions,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Comuna (Dropdown dependiente de región)
        ComunaDropdownSelector(
            selectedComuna = comuna,
            onComunaSelected = { comuna = it },
            comunas = comunaOptions,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isRegionSelected = region.isNotEmpty()
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

        Spacer(modifier = Modifier.height(16.dp))

        // Confirmar contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña *") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

        // Indicaciones
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = "• La contraseña debe tener al menos 6 caracteres",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• El teléfono debe tener exactamente 9 dígitos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de registro
        Button(
            onClick = {
                authViewModel.clearAuthResult()
                val direccionCompleta = Direccion(
                    calle = calle.trim(),
                    numero = numero.toIntOrNull() ?: 0,
                    comuna = comuna.trim(),
                    region = region.trim()
                )
                authViewModel.register(
                    rut = rut,
                    nombres = nombres,
                    apellidos = apellidos,
                    email = email,
                    passwd = passwd,
                    confirmPassword = confirmPassword,
                    telefono = telefono.takeIf { it.isNotBlank() },
                    direccion = direccionCompleta
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading &&
                    rut.isNotBlank() &&
                    nombres.isNotBlank() &&
                    apellidos.isNotBlank() &&
                    email.isNotBlank() &&
                    telefono.isNotBlank() &&
                    calle.isNotBlank() &&
                    numero.isNotBlank() &&
                    comuna.isNotBlank() &&
                    region.isNotBlank() &&
                    passwd.isNotBlank() &&
                    confirmPassword.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Crear Cuenta")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace a login
        Row {
            Text("¿Ya tienes cuenta? ")
            TextButton(
                onClick = onNavigateToLogin,
                enabled = !isLoading
            ) {
                Text("Inicia sesión")
            }
        }
    }
}
