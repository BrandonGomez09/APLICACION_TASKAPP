package com.example.tasksapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores personalizado Negro y Amarillo.
private val BlackAndYellowColorScheme = darkColorScheme(
    primary = VibrantYellow,          // El amarillo es el color principal para elementos interactivos.
    onPrimary = RichBlack,            // El texto sobre los botones amarillos será negro para máximo contraste.
    background = RichBlack,           // Fondo principal de la app.
    onBackground = SubtleWhite,       // Texto principal sobre el fondo negro.
    surface = DarkGray,               // Color para las superficies como las Cards.
    onSurface = SubtleWhite,          // Texto sobre las cards.
    secondary = MediumGray,           // Color para elementos secundarios.
    onSecondary = RichBlack,          // Texto sobre elementos secundarios.
    error = ErrorRed,                 // Rojo para errores.
    onError = RichBlack,
)

@Composable
fun TaksappTheme(
    content: @Composable () -> Unit
) {

    val colorScheme = BlackAndYellowColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}