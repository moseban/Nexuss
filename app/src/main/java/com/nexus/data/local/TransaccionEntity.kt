package com.nexus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad fundamental que representa la tabla 'transacciones' en la memoria flash del dispositivo.
 * Actúa como el espejo local de la estructura alojada en PostgreSQL.
 */
@Entity(tableName = "transacciones")
data class TransaccionEntity(
    @PrimaryKey val id: String,
    val concepto: String,
    val categoria: String,
    val monto: Double,
    val esIngreso: Boolean,
    val fecha: Long,
    val sincronizado: Boolean = false
)