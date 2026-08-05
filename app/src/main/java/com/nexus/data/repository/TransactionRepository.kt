package com.nexus.data.repository

import com.nexus.data.models.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import kotlin.math.absoluteValue

class TransactionRepository {

    private val _transactions = MutableStateFlow<List<Transaction>>(sampleTransactions())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    fun addTransaction(transaction: Transaction) {
        _transactions.value = _transactions.value + transaction
    }

    fun deleteAllTransactions() {
        _transactions.value = emptyList()
    }

    fun getBalance(): Double {
        return _transactions.value.sumOf { it.amount }
    }

    fun getTotalIncome(): Double {
        return _transactions.value.asSequence()
            .filter { it.amount > 0 }
            .sumOf { it.amount }
    }

    fun getTotalExpense(): Double {
        return _transactions.value.asSequence()
            .filter { it.amount < 0 }
            .sumOf { it.amount }
            .absoluteValue
    }

    fun getCategoryExpenses(): Map<String, Double> {
        return _transactions.value
            .filter { it.amount < 0 }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { transaction -> -transaction.amount } }
    }

    private fun sampleTransactions(): List<Transaction> {
        val calendar = Calendar.getInstance()
        return listOf(
            Transaction("1", "Supermercado", "Alimentación", -45.50, calendar.apply { set(2026, 4, 18) }.time, "14:30"),
            Transaction("2", "Salario", "Ingresos", 2500.00, calendar.apply { set(2026, 4, 15) }.time, "09:00"),
            Transaction("3", "Netflix", "Suscripciones", -12.99, calendar.apply { set(2026, 4, 14) }.time, "08:00"),
            Transaction("4", "Gasolina", "Transporte", -60.00, calendar.apply { set(2026, 4, 13) }.time, "18:45"),
            Transaction("5", "Restaurante", "Alimentación", -35.80, calendar.apply { set(2026, 4, 12) }.time, "20:15"),
            Transaction("6", "Freelance", "Ingresos", 450.00, calendar.apply { set(2026, 4, 10) }.time, null)
        )
    }
}