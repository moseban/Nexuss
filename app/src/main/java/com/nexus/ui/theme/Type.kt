package com.nexus.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Se establece el sistema tipográfico base.
 * La arquitectura visual brutalista requiere pesos extremos (Black/Bold) para
 * titulares y legibilidad absoluta para cuerpos de texto, eliminando ornamentos.
 */
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default, // Se proyecta la futura inyección de una fuente geométrica o monoespaciada
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        letterSpacing = (-1.5).sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 1.sp
    )
)