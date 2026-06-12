package com.nexus.ui.history

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
import com.nexus.viewmodel.TransactionUi
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    val transactions = remember { viewModel.getAllTransactions() }
    var searchText by remember { mutableStateOf("") }

    val filteredTransactions = transactions.filter {
        searchText.isEmpty() ||
                it.title.contains(searchText, ignoreCase = true) ||
                it.category.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transacciones") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar en historial") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTransactions) { transaction ->
                    HistoryItem(transaction)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(transaction: TransactionUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(transaction.title, style = MaterialTheme.typography.bodyLarge)
                Text(transaction.category, style = MaterialTheme.typography.bodySmall)
                Text(
                    "${transaction.dateFormatted} ${transaction.time ?: ""}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(
                text = String.format(Locale.getDefault(), "€%.2f", transaction.amount),
                color = if (transaction.amount > 0) Color(0xFF4CAF50) else Color(0xFFEF5350)
            )
        }
    }
}