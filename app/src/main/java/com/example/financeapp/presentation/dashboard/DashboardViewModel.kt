package com.example.financeapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.repository.AuthRepository
import com.example.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardData(
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val allTransactions: List<Transaction> = emptyList()
)

sealed class DashboardState {
    object Loading : DashboardState()
    data class Success(val data: DashboardData) : DashboardState()
    data class Error(val message: String) : DashboardState()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val state: StateFlow<DashboardState> = _state

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _state.value = DashboardState.Loading
            getTransactionsUseCase()
                .onSuccess { transactions ->
                    val income  = transactions
                        .filter { it.type == "income" }
                        .sumOf { it.amount }
                    val expense = transactions
                        .filter { it.type == "expense" }
                        .sumOf { it.amount }
                    _state.value = DashboardState.Success(
                        DashboardData(
                            totalBalance       = income - expense,
                            totalIncome        = income,
                            totalExpense       = expense,
                            recentTransactions = transactions.take(5),
                            allTransactions    = transactions
                        )
                    )
                }
                .onFailure {
                    _state.value = DashboardState.Error(it.message ?: "Error")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}