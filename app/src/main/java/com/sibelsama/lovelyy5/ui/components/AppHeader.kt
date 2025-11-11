package com.sibelsama.lovelyy5.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppHeader(isHome: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
        Text(
            text = "Lovely Y5 - App",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        if (isHome) {
            Text(
                text = "Tecnología al alcance de tu bolsillo",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
