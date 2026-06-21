// app/src/main/java/com/nexus/ui/theme/theme.kt

package com.nexus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Se ignora la condicional del sistema operativo (isSystemInDarkTheme).
 * El ecosistema impone un esquema de color oscuro inmutable para preservar
 * la identidad visual. Los colores referenciados asumen su existencia previa en Color.kt.
 */
private val NexusColorScheme = darkColorScheme(
    primary = NexusPurple,
    secondary = NexusGreen,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextSecondary
)

@Composable
fun AppNexusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NexusColorScheme,
        typography = Typography, // Se enlaza el archivo Type.kt
        content = content
    )
}