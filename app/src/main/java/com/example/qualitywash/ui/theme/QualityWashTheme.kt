package com.example.qualitywash.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores personalizados - Tema Lavandería
val Primary = Color(0xFF00A896)      // Verde turquesa (agua fresca)
val PrimaryDark = Color(0xFF05668D)  // Azul profundo (agua limpia)
val Accent = Color(0xFF02C39A)       // Verde menta (frescura)
val Background = Color(0xFFEDF6F9)   // Azul muy claro (espuma)
val Surface = Color(0xFFFFFFFF)      // Blanco (limpieza)
val Error = Color(0xFFFF5A5F)        // Rojo suave

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