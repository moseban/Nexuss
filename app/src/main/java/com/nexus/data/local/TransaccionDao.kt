package com.nexus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de manipulación de datos. Se establecen las operaciones atómicas de lectura y escritura
 * aislando el hilo principal de la interfaz gráfica.
 */
@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun obtenerTodasReactivo(): Flow<List<TransaccionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(transaccion: TransaccionEntity)

    @Query("SELECT * FROM transacciones WHERE sincronizado = 0")
    suspend fun obtenerPendientesDeSincronizacion(): List<TransaccionEntity>

    @Query("UPDATE transacciones SET sincronizado = 1 WHERE id IN (:ids)")
    suspend fun marcarComoSincronizadas(ids: List<String>)
}