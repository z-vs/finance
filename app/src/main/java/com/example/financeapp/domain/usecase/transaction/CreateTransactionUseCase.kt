package com.example.financeapp.domain.usecase.transaction

import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        categoryId: Int,
        amount: Double,
        type: String,
        description: String?,
        date: Long
    ): Result<Transaction> = transactionRepository.createTransaction(
        categoryId, amount, type, description, date
    )
}