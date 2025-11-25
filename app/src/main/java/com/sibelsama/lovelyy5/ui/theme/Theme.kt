package com.sibelsama.lovelyy5.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFD5799), // enf_pink
    secondary = Color(0xFFFFC9DC), // light_pink
    tertiary = Color(0xFFFFACCA), // pink
    background = Color(0xFF260303), // ebano
    surface = Color(0xFF260303), // ebano
    onPrimary = Color(0xFFFFFFFF), // white
    onSecondary = Color(0xFFFFFFFF), // white
    onTertiary = Color(0xFFFFFFFF), // white
    onBackground = Color(0xFFFFFFFF), // white
    onSurface = Color(0xFFFFFFFF), // white
    error = Color(0xFFFF0000), // red
    onError = Color(0xFFFFFFFF),
    // Colores adicionales para la app
    primaryContainer = Color(0xFFB36AE2), // purple theme color
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF3A2F3F), // dark background variant
    onSecondaryContainer = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF4CAF50), // success green
    onErrorContainer = Color(0xFFFFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFD5799), // enf_pink
    secondary = Color(0xFFFFC9DC), // light_pink
    tertiary = Color(0xFFFFACCA), // pink
    background = Color(0xFFFFFFFF), // white
    surface = Color(0xFFFFFFFF), // white
    onPrimary = Color(0xFFFFFFFF), // white
    onSecondary = Color(0xFFFFFFFF), // white
    onTertiary = Color(0xFFFFFFFF), // white
    onBackground = Color(0xFF260303), // ebano
    onSurface = Color(0xFF260303), // ebano
    error = Color(0xFFFF0000), // red
    onError = Color(0xFFFFFFFF),
    // Colores adicionales para la app
    primaryContainer = Color(0xFFB36AE2), // purple theme color
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF7F3F8), // light background
    onSecondaryContainer = Color(0xFF260303),
    errorContainer = Color(0xFF4CAF50), // success green
    onErrorContainer = Color(0xFFFFFFFF)
)

@Composable
fun LovelyY5APPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Assuming Typography is defined in Typography.kt
        content = content
    )
}
