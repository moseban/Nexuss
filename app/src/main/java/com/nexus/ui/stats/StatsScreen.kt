package com.nexus.ui.stats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.ui.theme.*
import com.nexus.viewmodel.MainViewModel
import java.util.Locale

import androidx.compose.ui.text.TextStyle

@Composable
fun StatsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
) {
    val income by viewModel.income.collectAsState()
    val expense by viewModel.expense.collectAsState()
    val categoryStats by viewModel.categoryStats.collectAsState()

    Scaffold(
        containerColor = BackgroundDark
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp)
        ) {
            item {
                Text(
                    text = "Estadísticas Generales",
                    style = TextStyle(
                        brush = PrimaryGradient,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Flujo Entrante",
                        value = "€${String.format(Locale.getDefault(), "%,.2f", income)}",
                        change = "+12%",
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        accentColor = NexusGreen
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Flujo Saliente",
                        value = "€${String.format(Locale.getDefault(), "%,.2f", expense)}",
                        change = "-8%",
                        icon = Icons.AutoMirrored.Filled.TrendingDown,
                        accentColor = NexusPurple
                    )
                }
            }

            item {
                DistributionCard(categoryStats)
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color
) {
    Surface(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(24.dp),
        color = SurfaceDark,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = TextSecondary, fontSize = 14.sp)
            Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(change, color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun DistributionCard(stats: List<com.nexus.data.models.CategoryStats>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = SurfaceDark,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Distribución Holográfica",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Donut Chart
            val chartColors = listOf(
                NexusPurple,
                NexusGreen,
                Color(0xFF3498DB), // Azul
                Color(0xFFE74C3C), // Rojo
                Color(0xFFF1C40F), // Amarillo
                Color(0xFF9B59B6)  // Lavanda
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                Canvas(modifier = Modifier.size(180.dp)) {
                    var startAngle = -90f
                    stats.forEachIndexed { index, stat ->
                        val sweepAngle = (stat.percentage.toFloat() / 100f) * 360f
                        drawArc(
                            color = chartColors[index % chartColors.size],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle - 4f, // Gap entre segmentos
                            useCenter = false,
                            style = Stroke(width = 30.dp.toPx(), cap = StrokeCap.Round)
                        )
                        startAngle += sweepAngle
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Legend
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val chunkedStats = stats.chunked(2)
                chunkedStats.forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEachIndexed { index, stat ->
                            LegendItem(
                                modifier = Modifier.weight(1f),
                                name = stat.name,
                                percentage = "${stat.percentage}%",
                                color = chartColors[if (pair.size == 1) stats.size - 1 else (stats.indexOf(stat)) % chartColors.size]
                            )
                        }
                        if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(modifier: Modifier = Modifier, name: String, percentage: String, color: Color) {
    Surface(
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color.Black.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = name,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            Text(
                text = percentage,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}