package com.nexus.ui.home

/**
 * Se estructuran las entidades inmutables que representan el estado de la pantalla principal.
 * Esta estructura replica la respuesta analítica del servidor backend.
 */
data class Transaccion(
    val id: String,
    val concepto: String,
    val categoria: String,
    val monto: Double,
    val esIngreso: Boolean,
    val fechaStr: String
)

data class HomeState(
    val balanceTotal: Double = 0.0,
    val ingresosMensuales: Double = 0.0,
    val gastosMensuales: Double = 0.0,
    val prediccion: Double = 0.0,
    val autoAhorro: Double = 0.0,
    val metaPro: Double = 0.0,
    val transaccionesRecientes: List<Transaccion> = emptyList(),
    val estaCargando: Boolean = true
)