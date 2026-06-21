package com.nexus.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.data.local.TransaccionDao
import com.nexus.data.local.TransaccionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Controlador asíncrono acoplado a la inyección de dependencias.
 * Se transforma el flujo de la base de datos local directamente en el estado
 * consumible por la interfaz gráfica.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transaccionDao: TransaccionDao
) : ViewModel() {

    val estado: StateFlow<HomeState> = transaccionDao.obtenerTodasReactivo()
        .map { listaEntidades ->
            val ingresos = listaEntidades.filter { it.esIngreso }.sumOf { it.monto }
            val gastos = listaEntidades.filter { !it.esIngreso }.sumOf { it.monto }
            val balance = ingresos - gastos

            // Se asume la existencia previa de una función de mapeo (mapper)
            // de TransaccionEntity a la clase Transaccion utilizada por la vista.
            val transaccionesVista = listaEntidades.map { entidad ->
                Transaccion(
                    id = entidad.id,
                    concepto = entidad.concepto,
                    categoria = entidad.categoria,
                    monto = entidad.monto,
                    esIngreso = entidad.esIngreso,
                    fechaStr = "Hoy" // Formateo de fecha pendiente
                )
            }

            HomeState(
                balanceTotal = balance,
                ingresosMensuales = ingresos,
                gastosMensuales = gastos,
                transaccionesRecientes = transaccionesVista,
                estaCargando = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeState(estaCargando = true)
        )

    fun agregarTransaccion(concepto: String, categoria: String, monto: Double, esIngreso: Boolean) {
        viewModelScope.launch {
            val nuevaEntidad = TransaccionEntity(
                id = System.currentTimeMillis().toString(),
                concepto = concepto,
                categoria = categoria,
                monto = monto,
                esIngreso = esIngreso,
                fecha = System.currentTimeMillis()
            )
            transaccionDao.insertar(nuevaEntidad)
        }
    }
}