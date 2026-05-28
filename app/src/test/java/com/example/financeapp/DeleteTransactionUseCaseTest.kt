package com.example.financeapp

import com.example.financeapp.domain.repository.TransactionRepository
import com.example.financeapp.domain.usecase.transaction.DeleteTransactionUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DeleteTransactionUseCaseTest {

    private val repository: TransactionRepository = mock()
    private val useCase = DeleteTransactionUseCase(repository)

    @Test
    fun `успешно удаляет транзакцию`() = runTest {
        whenever(repository.deleteTransaction(1)).thenReturn(Result.success(Unit))

        val result = useCase(1)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `возвращает ошибку если транзакция не найдена`() = runTest {
        whenever(repository.deleteTransaction(999)).thenReturn(
            Result.failure(RuntimeException("Not found"))
        )

        val result = useCase(999)

        assertTrue(result.isFailure)
        assertEquals("Not found", result.exceptionOrNull()?.message)
    }
}