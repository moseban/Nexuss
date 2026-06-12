package com.nexus.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.viewmodel.MainViewModel
import com.nexus.viewmodel.TransactionUi
import java.text.DecimalFormat

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToAdd: () -> Unit
) {
    val balance by viewModel.balance.collectAsState()
    val income by viewModel.income.collectAsState()
    val expense by viewModel.expense.collectAsState()
    val predictions by viewModel.predictions.collectAsState()
    val autoSave by viewModel.autoSave.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()

    val currencyFormat = DecimalFormat("€#,##0.00")
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", fontSize = 24.sp)
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        onNavigateToHistory()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Historial"
                        )
                    },
                    label = { Text("Historial") }
                )
                NavigationBarItem(
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        onNavigateToStats()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Estadísticas"
                        )
                    },
                    label = { Text("AI Stats") }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Nexus Wallet", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Balance Optimizado", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = currencyFormat.format(balance),
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Ingresos", style = MaterialTheme.typography.bodySmall)
                                Text(currencyFormat.format(income), color = Color(0xFF4CAF50))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Gastos", style = MaterialTheme.typography.bodySmall)
                                Text(currencyFormat.format(expense), color = Color(0xFFEF5350))
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoChip("Predicción", currencyFormat.format(predictions))
                    InfoChip("Auto-Ahorro", currencyFormat.format(autoSave))
                    InfoChip("Meta Pro", "€1,500")
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Actividad Reciente", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = { onNavigateToHistory() }) {
                        Text("Ver todas")
                    }
                }
            }

            items(recentTransactions) { transaction ->
                TransactionItem(transaction, currencyFormat)
            }
        }
    }
}

@Composable
fun InfoChip(title: String, value: String) {
    Card(
        modifier = Modifier.width(110.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionUi, currencyFormat: DecimalFormat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(transaction.title, style = MaterialTheme.typography.bodyLarge)
                Text(transaction.category, style = MaterialTheme.typography.bodySmall)
                Text(transaction.dateFormatted, style = MaterialTheme.typography.labelSmall)
            }
            Text(
                text = currencyFormat.format(transaction.amount),
                color = if (transaction.amount > 0) Color(0xFF4CAF50) else Color(0xFFEF5350)
            )
        }
    }
}