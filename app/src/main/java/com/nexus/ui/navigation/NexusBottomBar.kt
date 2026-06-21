package com.nexus.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nexus.ui.theme.BackgroundDark
import com.nexus.ui.theme.NexusGreen
import com.nexus.ui.theme.PrimaryGradient
import com.nexus.ui.theme.TextSecondary

@Composable
fun NexusBottomBar(navController: NavController) {
    val items = listOf(Screen.Home, Screen.Historial, Screen.ActionCenter, Screen.Stats, Screen.Perfil)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundDark) // Fondo abisal
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { screen ->
            if (screen == Screen.ActionCenter) {
                // Nodo central: Botón flotante con gradiente
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(PrimaryGradient)
                        .clickable {

                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Icono del escáner en blanco
                    Text("O", color = Color.White, fontSize = 24.sp)
                }
            } else {
                // Nodos estándar
                val isSelected = currentRoute == screen.route
                val contentColor = if (isSelected) NexusGreen else TextSecondary

                Column(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    screen.icon?.let {
                        Icon(imageVector = it, contentDescription = screen.title, tint = contentColor)
                    }
                    Text(text = screen.title, color = contentColor, fontSize = 10.sp)
                }
            }
        }
    }
}