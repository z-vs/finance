package com.example.financeapp.domain.usecase.transaction

import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        id: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: Long
    ): Result<Transaction> = transactionRepository.updateTransaction(
        id, categoryId, amount, description, date
    )
}