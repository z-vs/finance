package com.example.financeapp

import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.repository.TransactionRepository
import com.example.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetTransactionsUseCaseTest {

    private val repository: TransactionRepository = mock()
    private val useCase = GetTransactionsUseCase(repository)

    private fun makeTransaction(id: Int) = Transaction(
        id = id,
        categoryId = 1,
        categoryName = "Еда",
        amount = 100.0,
        type = "expense",
        description = null,
        date = 1000L
    )

    @Test
    fun `возвращает список транзакций при успехе`() = runTest {
        val expected = listOf(makeTransaction(1), makeTransaction(2))
        whenever(repository.getTransactions()).thenReturn(Result.success(expected))

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `возвращает пустой список если транзакций нет`() = runTest {
        whenever(repository.getTransactions()).thenReturn(Result.success(emptyList()))

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(emptyList<Transaction>(), result.getOrNull())
    }

    @Test
    fun `возвращает ошибку если репозиторий упал`() = runTest {
        whenever(repository.getTransactions()).thenReturn(
            Result.failure(RuntimeException("Network error"))
        )

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}