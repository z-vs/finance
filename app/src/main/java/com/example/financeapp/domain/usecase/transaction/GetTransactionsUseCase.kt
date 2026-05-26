package com.example.financeapp.domain.usecase.transaction

import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(): Result<List<Transaction>> =
        transactionRepository.getTransactions()
}