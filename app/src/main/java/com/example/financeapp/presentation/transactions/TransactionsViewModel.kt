package com.example.financeapp.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.usecase.transaction.CreateTransactionUseCase
import com.example.financeapp.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.example.financeapp.domain.usecase.transaction.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TransactionsState {
    object Loading : TransactionsState()
    data class Success(val transactions: List<Transaction>) : TransactionsState()
    data class Error(val message: String) : TransactionsState()
}

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<TransactionsState>(TransactionsState.Loading)
    val state: StateFlow<TransactionsState> = _state

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _state.value = TransactionsState.Loading
            getTransactionsUseCase()
                .onSuccess { _state.value = TransactionsState.Success(it) }
                .onFailure { _state.value = TransactionsState.Error(it.message ?: "Error") }
        }
    }

    fun createTransaction(
        categoryId: Int,
        amount: Double,
        type: String,
        description: String?,
        date: Long
    ) {
        viewModelScope.launch {
            createTransactionUseCase(categoryId, amount, type, description, date)
                .onSuccess { loadTransactions() }
                .onFailure { _state.value = TransactionsState.Error(it.message ?: "Error") }
        }
    }

    fun updateTransaction(
        id: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: Long
    ) {
        viewModelScope.launch {
            updateTransactionUseCase(id, categoryId, amount, description, date)
                .onSuccess { loadTransactions() }
                .onFailure { _state.value = TransactionsState.Error(it.message ?: "Error") }
        }
    }

    fun deleteTransaction(id: Int) {
        viewModelScope.launch {
            deleteTransactionUseCase(id)
                .onSuccess { loadTransactions() }
                .onFailure { _state.value = TransactionsState.Error(it.message ?: "Error") }
        }
    }
}