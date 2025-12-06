package com.sibelsama.lovelyy5.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.viewmodels.AuthViewModel
import com.sibelsama.lovelyy5.model.User
import com.sibelsama.lovelyy5.model.Direccion
import com.sibelsama.lovelyy5.data.CatApiService
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.material3.CircularProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val authResult by authViewModel.authResult.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }

    // Estados para imagen de perfil
    var currentProfileImage by remember { mutableStateOf("") }
    var isLoadingNewImage by remember { mutableStateOf(false) }
    val catApiService = remember { CatApiService() }

    // Inicializar campos cuando se carga el usuario
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            nombres = user.nombres
            apellidos = user.apellidos
            telefono = user.telefono.toString()
            calle = user.direccion.calle
            numero = user.direccion.numero.toString()
            comuna = user.direccion.comuna
            region = user.direccion.region
            currentProfileImage = user.fotoPerfil.ifEmpty {
                // Si no tiene imagen, cargar una aleatoria
                catApiService.getRandomCatImage() ?: catApiService.getFallbackCatImage()
            }
        }
    }

    // Mostrar mensaje cuando se actualiza exitosamente
    authResult?.let { result ->
        if (result.isSuccess && isEditing) {
            LaunchedEffect(result) {
                isEditing = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Mi Perfil") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            },
            actions = {
                if (isEditing) {
                    TextButton(
                        onClick = { isEditing = false }
                    ) {
                        Text("Cancelar")
                    }
                } else {
                    IconButton(
                        onClick = { isEditing = true }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                }
            }
        )

        if (currentUser == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Column
        }

        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Información del usuario
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = currentUser!!.displayName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "RUT: ${currentUser!!.formattedRut}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = currentUser!!.email,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Rol: ${currentUser!!.rol.replaceFirstChar { it.uppercase() }}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Formulario de datos personales
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Datos Personales",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = nombres,
                        onValueChange = { nombres = it },
                        label = { Text("Nombres *") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = apellidos,
                        onValueChange = { apellidos = it },
                        label = { Text("Apellidos *") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono *") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("912345678") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            // Dirección
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Dirección de Envío",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = calle,
                        onValueChange = { calle = it },
                        label = { Text("Calle *") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Av. Providencia") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = numero,
                            onValueChange = { numero = it },
                            label = { Text("Número *") },
                            enabled = isEditing,
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("123") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = comuna,
                            onValueChange = { comuna = it },
                            label = { Text("Comuna *") },
                            enabled = isEditing,
                            modifier = Modifier.weight(2f),
                            placeholder = { Text("Las Condes") }
                        )
                    }

                    OutlinedTextField(
                        value = region,
                        onValueChange = { region = it },
                        label = { Text("Región *") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Región Metropolitana") }
                    )
                }
            }

            // Mostrar error si existe
            authResult?.let { result ->
                if (!result.isSuccess && result.errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = result.errorMessage,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // Botones de acción
            if (isEditing) {
                Button(
                    onClick = {
                        currentUser?.let { user ->
                            val updatedDireccion = Direccion(
                                calle = calle.trim(),
                                numero = numero.toIntOrNull() ?: 0,
                                comuna = comuna.trim(),
                                region = region.trim()
                            )
                            val updatedUser = user.copy(
                                nombres = nombres.trim(),
                                apellidos = apellidos.trim(),
                                telefono = telefono.toLongOrNull() ?: 0,
                                direccion = updatedDireccion
                            )
                            authViewModel.updateProfile(updatedUser)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading &&
                            nombres.isNotBlank() &&
                            apellidos.isNotBlank() &&
                            telefono.isNotBlank() &&
                            calle.isNotBlank() &&
                            numero.isNotBlank() &&
                            comuna.isNotBlank() &&
                            region.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Guardar Cambios")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de cerrar sesión
            OutlinedButton(
                onClick = {
                    authViewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}
