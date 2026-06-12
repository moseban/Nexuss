package com.nexus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexus.ui.history.HistoryScreen
import com.nexus.ui.home.HomeScreen
import com.nexus.ui.navigation.Screen
import com.nexus.ui.stats.StatsScreen
import com.nexus.ui.theme.AppNexusTheme
import com.nexus.viewmodel.MainViewModel
import com.nexus.ui.add.AddTransactionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNexusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: MainViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {

                        composable(Screen.Add.route) {
                            AddTransactionScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToHistory = {
                                    navController.navigate(Screen.History.route)
                                },
                                onNavigateToStats = {
                                    navController.navigate(Screen.Stats.route)
                                },
                                onNavigateToAdd = {
                                    navController.navigate(Screen.Add.route)
                                }
                            )
                        }

                        composable(Screen.History.route) {
                            HistoryScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Screen.Stats.route) {
                            StatsScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}