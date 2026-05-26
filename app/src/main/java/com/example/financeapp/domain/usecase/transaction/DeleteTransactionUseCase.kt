package com.example.financeapp.domain.usecase.transaction

import com.example.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> =
        transactionRepository.deleteTransaction(id)
}