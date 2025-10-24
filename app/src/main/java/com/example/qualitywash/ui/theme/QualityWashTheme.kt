package com.example.qualitywash.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores personalizados
val Primary = Color(0xFF667eea)
val PrimaryDark = Color(0xFF5568d3)
val Accent = Color(0xFFf093fb)
val Background = Color(0xFFF5F5F5)
val Surface = Color(0xFFFFFFFF)
val Error = Color(0xFFF44336)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Accent,
    tertiary = PrimaryDark,
    error = Error
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Accent,
    tertiary = PrimaryDark,
    background = Background,
    surface = Surface,
    error = Error
)

@Composable
fun QualityWashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}