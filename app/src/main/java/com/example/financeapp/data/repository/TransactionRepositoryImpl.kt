package com.example.financeapp.data.repository

import com.example.financeapp.data.remote.api.TransactionApi
import com.example.financeapp.data.remote.dto.CreateTransactionRequest
import com.example.financeapp.data.remote.dto.UpdateTransactionRequest
import com.example.financeapp.data.remote.toUserMessage
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
                Transaction(it.id, it.categoryId, it.categoryName,
                    it.amount, it.type, it.description, it.date)
            })
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage()))
        }

    override suspend fun createTransaction(
        categoryId: Int, amount: Double, type: String,
        description: String?, date: Long
    ): Result<Transaction> =
        try {
            val response = transactionApi.createTransaction(
                CreateTransactionRequest(categoryId, amount, type, description, date)
            )
            Result.success(Transaction(response.id, response.categoryId, response.categoryName,
                response.amount, response.type, response.description, response.date))
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage()))
        }

    override suspend fun updateTransaction(
        id: Int, categoryId: Int, amount: Double,
        description: String?, date: Long
    ): Result<Transaction> =
        try {
            val response = transactionApi.updateTransaction(
                id, UpdateTransactionRequest(categoryId, amount, description, date)
            )
            Result.success(Transaction(response.id, response.categoryId, response.categoryName,
                response.amount, response.type, response.description, response.date))
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage()))
        }

    override suspend fun deleteTransaction(id: Int): Result<Unit> =
        try {
            transactionApi.deleteTransaction(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage()))
        }
}