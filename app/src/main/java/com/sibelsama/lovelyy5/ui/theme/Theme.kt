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

private val AppPrimary = Color(0xFFB36AE2)
private val AppPrimaryVariant = Color(0xFF8A4BD0)
private val AppSecondary = Color(0xFFFDE7F3)
private val AppTertiary = Color(0xFFE3D6F7)
private val AppBackground = Color(0xFFF7F3F8)
private val AppOnBackground = Color(0xFF260303)

private val DarkColorScheme = darkColorScheme(
    primary = AppPrimary,
    secondary = AppSecondary,
    tertiary = AppTertiary,
    background = AppBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = AppOnBackground,
    onTertiary = AppOnBackground,
    onBackground = AppOnBackground,
    onSurface = AppOnBackground,
    error = Color(0xFFFF0000),
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = AppPrimary,
    secondary = AppSecondary,
    tertiary = AppTertiary,
    background = AppBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = AppOnBackground,
    onTertiary = AppOnBackground,
    onBackground = AppOnBackground,
    onSurface = AppOnBackground,
    error = Color(0xFFFF0000),
    onError = Color.White
)

@Composable
fun LovelyY5APPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
        typography = Typography,
        content = content
    )
}
