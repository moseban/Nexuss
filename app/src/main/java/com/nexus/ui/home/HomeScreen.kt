package com.nexus.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexus.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToAdd: () -> Unit
) {
    // La interfaz observa reactivamente los cambios en el estado
    val estado by viewModel.estado.collectAsState()

    if (estado.estaCargando) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NexusPurple)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        // Encabezado
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Nexus Wallet", fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextPrimary)

                // Botón de Inserción Manual Superior
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PrimaryGradient)
                        .clickable { onNavigateToAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar Transacción Manual",
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Tarjeta Principal de Balance
        item {
            TarjetaBalanceGlobal(estado)
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Lista de Transacciones (Actividad Reciente)
        item {
            Text(text = "Actividad Reciente", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(estado.transaccionesRecientes) { transaccion ->
            FilaTransaccion(transaccion)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun TarjetaBalanceGlobal(estado: HomeState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        Column {
            Text(text = "Balance Optimizado", color = TextSecondary, fontSize = 14.sp)
            Text(
                text = "€${estado.balanceTotal}",
                color = TextPrimary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black
            )
            // Aquí se anidarían las sub-tarjetas de Ingresos y Gastos
        }
    }
}

@Composable
private fun FilaTransaccion(tx: Transaccion) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = tx.concepto, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = tx.categoria, color = TextSecondary, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            val colorMonto = if (tx.esIngreso) NexusGreen else TextPrimary
            val signo = if (tx.esIngreso) "+" else "-"
            Text(text = "$signo€${tx.monto}", color = colorMonto, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = tx.fechaStr, color = TextSecondary, fontSize = 12.sp)
        }
    }
}