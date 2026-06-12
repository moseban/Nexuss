package com.nexus.ui.add

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nexus.data.models.Transaction
import com.nexus.viewmodel.MainViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Alimentación") }
    var selectedType by remember { mutableStateOf("Gasto") } // "Ingreso" o "Gasto"
    var showCategoryDialog by remember { mutableStateOf(false) }

    val categories = listOf(
        "Alimentación", "Transporte", "Suscripciones",
        "Salud", "Compras", "Entretenimiento", "Servicios", "Otros"
    )

    val incomeCategories = listOf("Salario", "Freelance", "Regalos", "Inversiones", "Otros")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Transacción") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (title.isNotBlank() && amount.isNotBlank()) {
                                val amountValue = amount.toDoubleOrNull() ?: 0.0
                                val finalAmount = if (selectedType == "Gasto") -amountValue else amountValue

                                val transaction = Transaction(
                                    id = UUID.randomUUID().toString(),
                                    title = title,
                                    category = selectedCategory,
                                    amount = finalAmount,
                                    date = Date(),
                                    time = java.text.SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                )
                                viewModel.addTransaction(transaction)
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selector de tipo (Ingreso/Gasto)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilterChip(
                    selected = selectedType == "Gasto",
                    onClick = { selectedType = "Gasto"; selectedCategory = categories.first() },
                    label = { Text("Gasto") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedType == "Ingreso",
                    onClick = { selectedType = "Ingreso"; selectedCategory = incomeCategories.first() },
                    label = { Text("Ingreso") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Campo título
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Concepto") },
                placeholder = { Text("Ej: Supermercado, Salario...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo monto
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monto") },
                placeholder = { Text("0.00") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Text("€") },
                singleLine = true
            )

            // Selector de categoría
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showCategoryDialog = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Categoría", style = MaterialTheme.typography.labelMedium)
                    Text(selectedCategory, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón guardar (alternativo)
            Button(
                onClick = {
                    if (title.isNotBlank() && amount.isNotBlank()) {
                        val amountValue = amount.toDoubleOrNull() ?: 0.0
                        val finalAmount = if (selectedType == "Gasto") -amountValue else amountValue

                        val transaction = Transaction(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            category = selectedCategory,
                            amount = finalAmount,
                            date = Date(),
                            time = java.text.SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                        )
                        viewModel.addTransaction(transaction)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && amount.isNotBlank()
            ) {
                Text("Guardar Transacción")
            }
        }
    }

    // Diálogo para seleccionar categoría
    if (showCategoryDialog) {
        Dialog(onDismissRequest = { showCategoryDialog = false }) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Seleccionar Categoría", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    val currentCategories = if (selectedType == "Gasto") categories else incomeCategories

                    currentCategories.forEach { category ->
                        TextButton(
                            onClick = {
                                selectedCategory = category
                                showCategoryDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(category, modifier = Modifier.fillMaxWidth())
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}