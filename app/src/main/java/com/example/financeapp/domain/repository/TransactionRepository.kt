package com.example.financeapp.domain.repository

import com.example.financeapp.domain.model.Transaction

interface TransactionRepository {
    suspend fun getTransactions(): Result<List<Transaction>>
    suspend fun createTransaction(
        categoryId: Int,
        amount: Double,
        type: String,
        description: String?,
        date: Long
    ): Result<Transaction>
    suspend fun updateTransaction(
        id: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: Long
    ): Result<Transaction>
    suspend fun deleteTransaction(id: Int): Result<Unit>
}