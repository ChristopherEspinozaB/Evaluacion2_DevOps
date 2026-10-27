package com.example.supletanes.ui.theme

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

//Paleta para modo claro
private val LightColorScheme = lightColorScheme(
    primary = AzulAcero,           //Color principal para elementos interactivos y encabezados.
    secondary = RojoEnergetico,     //Color de acento para botones de acción (CTA).
    tertiary = VerdeLima,          //Color opcional para destacar ofertas o demás.
    background = BlancoPuro,         //Fondo principal de la app.
    surface = BlancoPuro,            //Superficie de componentes como Cards, Menus.
    onPrimary = BlancoPuro,          //Texto/iconos sobre el color primario (AzulAcero).
    onSecondary = BlancoPuro,        //Texto/iconos sobre el color secundario (RojoEnergetico).
    onTertiary = BlancoPuro,         //Texto/iconos sobre el color terciario.
    onBackground = NegroTexto,       //Texto principal sobre el fondo.
    onSurface = NegroTexto,          //Texto sobre las superficies.
    outline = GrisPlata              //Bordes de campos de texto y botones "outlined".
)
private val DarkColorScheme = darkColorScheme(
    primary = AzulAcero,
    secondary = RojoEnergetico,
    tertiary = VerdeLima,
    background = Color(0xFF1C1B1F), //Fondo oscuro estándar
    surface = Color(0xFF1C1B1F),
    onPrimary = BlancoPuro,
    onSecondary = BlancoPuro,
    onTertiary = BlancoPuro,
    onBackground = Color(0xFFE6E1E5), //Texto claro para modo oscuro
    onSurface = Color(0xFFE6E1E5),
    outline = GrisPlata
)

@Composable
fun SupletanesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // El soporte para colores dinámicos es opcional pero una buena práctica en Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        //Si dynamicColor es true y el dispositivo es Android 12+, usa los colores del sistema
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        //Si no, usa nuestras paletas personalizadas
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        // SideEffect ejecuta este bloque cuando el Composable entra en la composición
        SideEffect {
            val window = (view.context as Activity).window

            // 2. Le decimos al sistema si los iconos de la barra de estado deben ser oscuros o claros.
            //    isAppearanceLightStatusBars = true -> Iconos oscuros (para fondos claros)
            //    isAppearanceLightStatusBars = false -> Iconos claros (para fondos oscuros)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, //Aplicar tipografía personalizada
        content = content
    )
}