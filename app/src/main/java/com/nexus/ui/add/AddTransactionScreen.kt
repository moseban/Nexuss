package com.nexus.ui.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onSave: (concepto: String, categoria: String, monto: Double, esIngreso: Boolean) -> Unit,
    onCancel: () -> Unit
) {
    var concepto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var esIngreso by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp)
    ) {
        Text("Nueva Transacción", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(32.dp))

        // Selector de Tipo (Ingreso / Gasto)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            FilterChip(
                selected = esIngreso,
                onClick = { esIngreso = true },
                label = { Text("Ingreso") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NexusGreen)
            )
            FilterChip(
                selected = !esIngreso,
                onClick = { esIngreso = false },
                label = { Text("Gasto") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NexusPurple)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("Monto (€)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = TextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = concepto,
            onValueChange = { concepto = it },
            label = { Text("Concepto (Ej. Supermercado)") },
            colors = TextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría (Ej. Alimentación)") },
            colors = TextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onCancel) {
                Text("Cancelar", color = TextSecondary)
            }
            Button(
                onClick = {
                    val montoDouble = monto.toDoubleOrNull() ?: 0.0
                    if (concepto.isNotBlank() && montoDouble > 0) {
                        onSave(concepto, categoria, montoDouble, esIngreso)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if(esIngreso) NexusGreen else NexusPurple)
            ) {
                Text("Registrar Nodo", color = BackgroundDark, fontWeight = FontWeight.Bold)
            }
        }
    }
}