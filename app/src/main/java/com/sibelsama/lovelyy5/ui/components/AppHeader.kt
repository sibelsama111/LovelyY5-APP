package com.sibelsama.lovelyy5.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sibelsama.lovelyy5.R

@Composable
fun AppHeader(isHome: Boolean = false) {
    Row(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo placeholder a la izquierda
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
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
}
