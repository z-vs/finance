package com.example.financeapp.data.repository

import com.example.financeapp.data.remote.api.TransactionApi
import com.example.financeapp.data.remote.dto.CreateTransactionRequest
import com.example.financeapp.data.remote.dto.UpdateTransactionRequest
import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionApi: TransactionApi
) : TransactionRepository {

    override suspend fun getTransactions(): Result<List<Transaction>> =
        try {
            val response = transactionApi.getTransactions()
            Result.success(response.map {
                Transaction(
                    id           = it.id,
                    categoryId   = it.categoryId,
                    categoryName = it.categoryName,
                    amount       = it.amount,
                    type         = it.type,
                    description  = it.description,
                    date         = it.date
                )
            })
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun createTransaction(
        categoryId: Int, amount: Double, type: String,
        description: String?, date: Long
    ): Result<Transaction> =
        try {
            val response = transactionApi.createTransaction(
                CreateTransactionRequest(categoryId, amount, type, description, date)
            )
            Result.success(
                Transaction(
                    id           = response.id,
                    categoryId   = response.categoryId,
                    categoryName = response.categoryName,
                    amount       = response.amount,
                    type         = response.type,
                    description  = response.description,
                    date         = response.date
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun updateTransaction(
        id: Int, categoryId: Int, amount: Double,
        description: String?, date: Long
    ): Result<Transaction> =
        try {
            val response = transactionApi.updateTransaction(
                id, UpdateTransactionRequest(categoryId, amount, description, date)
            )
            Result.success(
                Transaction(
                    id           = response.id,
                    categoryId   = response.categoryId,
                    categoryName = response.categoryName,
                    amount       = response.amount,
                    type         = response.type,
                    description  = response.description,
                    date         = response.date
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun deleteTransaction(id: Int): Result<Unit> =
        try {
            transactionApi.deleteTransaction(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}