package com.nexus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.data.models.CategoryStats
import com.nexus.data.models.Transaction
import com.nexus.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val repository = TransactionRepository()

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _income = MutableStateFlow(0.0)
    val income: StateFlow<Double> = _income.asStateFlow()

    private val _expense = MutableStateFlow(0.0)
    val expense: StateFlow<Double> = _expense.asStateFlow()

    private val _predictions = MutableStateFlow(0.0)
    val predictions: StateFlow<Double> = _predictions.asStateFlow()

    private val _autoSave = MutableStateFlow(0.0)
    val autoSave: StateFlow<Double> = _autoSave.asStateFlow()

    private val _recentTransactions = MutableStateFlow<List<TransactionUi>>(emptyList())
    val recentTransactions: StateFlow<List<TransactionUi>> = _recentTransactions.asStateFlow()

    private val _categoryStats = MutableStateFlow<List<CategoryStats>>(emptyList())
    val categoryStats: StateFlow<List<CategoryStats>> = _categoryStats.asStateFlow()

    init {
        loadDashboardData()
        loadCategoryStats()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            repository.transactions.collect { transactions ->
                _balance.value = repository.getBalance()
                _income.value = repository.getTotalIncome()
                _expense.value = repository.getTotalExpense()

                val lastExpense = transactions.filter { it.amount < 0 }
                    .maxByOrNull { it.date }?.amount?.absoluteValue ?: 0.0
                _predictions.value = lastExpense * 1.2

                _autoSave.value = (_income.value - _expense.value) * 0.3

                _recentTransactions.value = transactions.asSequence()
                    .sortedByDescending { it.date }
                    .take(3)
                    .map { it.toUi() }
                    .toList()
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        repository.addTransaction(transaction)
    }
    private fun loadCategoryStats() {
        viewModelScope.launch {
            repository.transactions.collect {
                val categoryMap = repository.getCategoryExpenses()
                val totalExpense = categoryMap.values.sum()

                val stats = categoryMap.map { (category, amount) ->
                    CategoryStats(
                        name = category,
                        percentage = if (totalExpense > 0) ((amount / totalExpense) * 100).toInt() else 0,
                        amount = amount
                    )
                }.sortedByDescending { it.percentage }

                _categoryStats.value = stats
            }
        }
    }

    fun getAllTransactions(): List<TransactionUi> {
        return repository.transactions.value.map { it.toUi() }
    }
}

data class TransactionUi(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val dateFormatted: String,
    val time: String?
)

private fun Transaction.toUi(): TransactionUi {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return TransactionUi(
        id = id,
        title = title,
        category = category,
        amount = amount,
        dateFormatted = formatter.format(date),
        time = time
    )
}