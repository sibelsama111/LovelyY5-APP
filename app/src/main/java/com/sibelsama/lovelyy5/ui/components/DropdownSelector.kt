package com.sibelsama.lovelyy5.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Dropdown selector reutilizable para formularios
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    placeholder: String = "Seleccionar...",
    isRequired: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded && enabled },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value.ifEmpty { if (expanded) "" else placeholder },
            onValueChange = { },
            label = {
                Text(if (isRequired) "$label *" else label)
            },
            leadingIcon = leadingIcon?.let { icon ->
                { Icon(icon, contentDescription = null) }
            },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Expandir menú",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            readOnly = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = if (value.isNotEmpty())
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            ),
            singleLine = true
        )

        if (options.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * Dropdown selector específico para regiones chilenas
 */
@Composable
fun RegionDropdownSelector(
    selectedRegion: String,
    onRegionSelected: (String) -> Unit,
    regions: List<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    DropdownSelector(
        label = "Región",
        value = selectedRegion,
        onValueChange = onRegionSelected,
        options = regions,
        modifier = modifier,
        enabled = enabled,
        leadingIcon = Icons.Default.Map,
        placeholder = "Seleccionar región...",
        isRequired = true
    )
}

/**
 * Dropdown selector específico para comunas chilenas
 */
@Composable
fun ComunaDropdownSelector(
    selectedComuna: String,
    onComunaSelected: (String) -> Unit,
    comunas: List<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isRegionSelected: Boolean = true
) {
    DropdownSelector(
        label = "Comuna",
        value = selectedComuna,
        onValueChange = onComunaSelected,
        options = comunas,
        modifier = modifier,
        enabled = enabled && isRegionSelected,
        leadingIcon = Icons.Default.LocationCity,
        placeholder = if (isRegionSelected) "Seleccionar comuna..." else "Primero selecciona una región",
        isRequired = true
    )
}
