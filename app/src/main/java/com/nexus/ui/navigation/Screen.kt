package com.nexus.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart // Placeholder para Stats/Analytics
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Contrato inmutable para la gestión de rutas en Jetpack Compose Navigation.
 */
sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object Login : Screen("login", "Login", null)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Historial : Screen("historial", "Historial", Icons.Default.List)
    object ActionCenter : Screen("action_center", "", null) 
    object Scanner : Screen("scanner", "Scanner", null)
    object Stats : Screen("stats", "Stats", Icons.Default.ShoppingCart)
    object Perfil : Screen("perfil", "Perfil", Icons.Default.Person)
}