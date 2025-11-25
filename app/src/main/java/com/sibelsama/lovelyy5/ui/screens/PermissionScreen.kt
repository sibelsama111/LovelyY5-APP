package com.sibelsama.lovelyy5.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.R
import com.sibelsama.lovelyy5.data.DataManager
import com.sibelsama.lovelyy5.utils.PermissionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionScreen(
    onPermissionsGranted: () -> Unit
) {
    val context = LocalContext.current
    val dataManager = remember { DataManager(context) }
    val scope = rememberCoroutineScope()

    var permissionsRequested by remember { mutableStateOf(false) }
    var showRationale by remember { mutableStateOf(false) }

    // Launcher para solicitar múltiples permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }

        scope.launch {
            dataManager.setPermissionsGranted(allGranted)
            dataManager.setNotFirstTime()
        }

        if (allGranted) {
            onPermissionsGranted()
        } else {
            showRationale = true
        }
    }

    // Verificar si ya tenemos todos los permisos
    LaunchedEffect(Unit) {
        if (PermissionManager.hasAllPermissions(context)) {
            dataManager.setPermissionsGranted(true)
            dataManager.setNotFirstTime()
            onPermissionsGranted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Logo o imagen de la app
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Logo de la app",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Bienvenido a LovelyY5!",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Para brindarte la mejor experiencia, necesitamos algunos permisos:",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Lista de permisos con iconos y explicaciones
        PermissionItem(
            icon = Icons.Default.Camera,
            title = "Cámara",
            description = "Para tomar fotos de tus valoraciones de productos"
        )

        Spacer(modifier = Modifier.height(16.dp))

        PermissionItem(
            icon = Icons.Default.Image,
            title = "Acceso a fotos",
            description = "Para seleccionar imágenes de tu galería"
        )

        Spacer(modifier = Modifier.height(16.dp))

        PermissionItem(
            icon = Icons.Default.Notifications,
            title = "Vibración",
            description = "Para feedback táctil en botones y acciones"
        )

        Spacer(modifier = Modifier.height(48.dp))

        if (!showRationale) {
            Button(
                onClick = {
                    permissionsRequested = true
                    permissionLauncher.launch(PermissionManager.REQUIRED_PERMISSIONS)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Conceder permisos",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    scope.launch {
                        dataManager.setPermissionsGranted(false)
                        dataManager.setNotFirstTime()
                    }
                    onPermissionsGranted() // Continuar sin permisos
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar sin permisos")
            }
        } else {
            // Mostrar cuando algunos permisos fueron denegados
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Algunos permisos fueron denegados",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Puedes continuar usando la app, pero algunas funciones pueden estar limitadas. Puedes cambiar los permisos más tarde en la configuración del dispositivo.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    permissionLauncher.launch(PermissionManager.REQUIRED_PERMISSIONS)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Intentar de nuevo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    scope.launch {
                        dataManager.setPermissionsGranted(false)
                        dataManager.setNotFirstTime()
                    }
                    onPermissionsGranted()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar de todas formas")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Al continuar, aceptas nuestros términos de uso y política de privacidad.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PermissionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
