package com.nexus.di

import android.content.Context
import androidx.room.Room
import com.nexus.data.local.NexusDatabase
import com.nexus.data.local.TransaccionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de infraestructura para la capa de persistencia local.
 * Se centraliza la instanciación para garantizar el uso de un patrón Singleton estricto.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun proveerBaseDeDatos(@ApplicationContext context: Context): NexusDatabase {
        return Room.databaseBuilder(
            context,
            NexusDatabase::class.java,
            "nexus_db_local"
        ).build()
    }

    @Provides
    @Singleton
    fun proveerTransaccionDao(database: NexusDatabase): TransaccionDao {
        return database.transaccionDao()
    }
}