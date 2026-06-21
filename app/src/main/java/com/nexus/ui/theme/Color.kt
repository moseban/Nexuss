package com.nexus.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

// Fondos y Superficies
val BackgroundDark = Color(0xFF0D0F14) // Fondo abisal observado en el Login
val SurfaceDark = Color(0xFF15181F) // Cajas de texto y tarjetas
val SurfaceBorder = Color(0xFF222630)

// Acentos
val NexusPurple = Color(0xFFA652FF)
val NexusGreen = Color(0xFF2ECC71)

// Tipografía
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF8A8F98)

// Utilidad de Gradiente Institucional
val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(NexusPurple, NexusGreen)
)