package com.nexus.data.models

import java.util.Date

data class Transaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,      // positivo = ingreso, negativo = gasto
    val date: Date,
    val time: String? = null
)