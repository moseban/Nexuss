package com.nexus.ui.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nexus.viewmodel.MainViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    val income by viewModel.income.collectAsState()
    val expense by viewModel.expense.collectAsState()
    val categoryStats by viewModel.categoryStats.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas Generales") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatBox("Flujo Entrante", "€${String.format(Locale.getDefault(), "%,.2f", income)}", "+12%", Color(0xFF4CAF50))
                        StatBox("Flujo Saliente", "€${String.format(Locale.getDefault(), "%,.2f", expense)}", "-8%", Color(0xFFEF5350))
                    }
                }
            }

            item {
                Text(
                    "Distribución por Categoría",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(categoryStats) { stat ->
                CategoryRow(stat.name, stat.percentage, stat.amount)
            }
        }
    }
}

@Composable
fun StatBox(title: String, value: String, change: String, color: Color) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.titleLarge)
        Text(change, style = MaterialTheme.typography.labelSmall, color = color)
    }
}

@Composable
fun CategoryRow(name: String, percentage: Int, amount: Double) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$name $percentage%")
            Text("€${String.format(Locale.getDefault(), "%,.2f", amount)}")
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF6200EE)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}