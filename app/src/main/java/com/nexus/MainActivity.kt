package com.nexus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nexus.ui.home.HomeScreen
import com.nexus.ui.navigation.NexusBottomBar
import com.nexus.ui.navigation.Screen
import com.nexus.ui.theme.AppNexusTheme
import com.nexus.ui.theme.BackgroundDark
import com.nexus.ui.home.HomeViewModel
import com.nexus.ui.add.AddTransactionScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import com.nexus.ui.auth.LoginScreen
import com.nexus.ui.history.HistoryScreen
import com.nexus.ui.stats.StatsScreen
import com.nexus.ui.profile.ProfileScreen
import com.nexus.ui.scanner.ScannerScreen
import com.nexus.ui.scanner.ScannerViewModel
import com.nexus.viewmodel.MainViewModel

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
    val mainViewModel: MainViewModel = hiltViewModel()
    val scannerViewModel: ScannerViewModel = hiltViewModel()

    Scaffold(
        bottomBar = { 
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute != Screen.Login.route && currentRoute != Screen.Scanner.route) {
                NexusBottomBar(navController = navController) 
            }
        },
        containerColor = BackgroundDark
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    )
                }

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
                            navController.popBackStack()
                        },
                        onCancel = {
                            navController.popBackStack()
                        }
                    )
                }
                
                composable(Screen.Historial.route) {
                    HistoryScreen(
                        viewModel = mainViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                
                composable(Screen.Stats.route) {
                    StatsScreen(
                        viewModel = mainViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                
                composable(Screen.Perfil.route) {
                    ProfileScreen(
                        onLogout = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Scanner.route) {
                    ScannerScreen(
                        viewModel = scannerViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}