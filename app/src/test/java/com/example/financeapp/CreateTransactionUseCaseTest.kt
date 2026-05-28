package com.example.financeapp

import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.repository.TransactionRepository
import com.example.financeapp.domain.usecase.transaction.CreateTransactionUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CreateTransactionUseCaseTest {

    private val repository: TransactionRepository = mock()
    private val useCase = CreateTransactionUseCase(repository)

    private val newTransaction = Transaction(
        id = 10,
        categoryId = 2,
        categoryName = "Транспорт",
        amount = 150.0,
        type = "expense",
        description = "Метро",
        date = 1000L
    )

    @Test
    fun `успешно создаёт транзакцию`() = runTest {
        whenever(
            repository.createTransaction(2, 150.0, "expense", "Метро", 1000L)
        ).thenReturn(Result.success(newTransaction))

        val result = useCase(2, 150.0, "expense", "Метро", 1000L)

        assertTrue(result.isSuccess)
        assertEquals(newTransaction, result.getOrNull())
    }

    @Test
    fun `возвращает ошибку при неверной категории`() = runTest {
        whenever(
            repository.createTransaction(999, 150.0, "expense", null, 1000L)
        ).thenReturn(Result.failure(RuntimeException("Invalid category")))

        val result = useCase(999, 150.0, "expense", null, 1000L)

        assertTrue(result.isFailure)
        assertEquals("Invalid category", result.exceptionOrNull()?.message)
    }

    @Test
    fun `сумма не может быть отрицательной`() = runTest {
        whenever(
            repository.createTransaction(1, -100.0, "expense", null, 1000L)
        ).thenReturn(Result.failure(RuntimeException("Amount must be positive")))

        val result = useCase(1, -100.0, "expense", null, 1000L)

        assertTrue(result.isFailure)
    }
}