// app/src/main/java/com/nexus/viewmodel/MainViewModel.kt
package com.nexus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexus.ui.home.HomeScreen
import com.nexus.ui.navigation.NexusBottomBar
import com.nexus.ui.navigation.Screen
import com.nexus.ui.theme.AppNexusTheme
import com.nexus.ui.theme.BackgroundDark
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexus.ui.home.HomeViewModel
import com.nexus.ui.add.AddTransactionScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNexusTheme {
                NexusAppShell()
            }
        }
    }
}

@Composable
fun NexusAppShell() {
    val navController = rememberNavController()
    val sharedHomeViewModel: HomeViewModel = hiltViewModel()

    Scaffold(
        bottomBar = { NexusBottomBar(navController = navController) },
        containerColor = BackgroundDark
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = sharedHomeViewModel,
                        onNavigateToAdd = { navController.navigate("add_transaction") }
                    )
                }

                composable("add_transaction") {
                    AddTransactionScreen(
                        onSave = { concepto, categoria, monto, esIngreso ->
                            sharedHomeViewModel.agregarTransaccion(concepto, categoria, monto, esIngreso)
                            navController.popBackStack() // Retorna al Home tras inyectar el dato
                        },
                        onCancel = {
                            navController.popBackStack() // Retorna al Home si se cancela
                        }
                    )
                }
                // Implementación temporal de vistas vacías     para mantener la integridad de compilación
                composable(Screen.Historial.route) { /* HistorialScreen() */ }
                composable(Screen.Stats.route) { /* StatsScreen() */ }
                composable(Screen.Perfil.route) { /* PerfilScreen() */ }
            }
        }
    }
}