package com.example.financeapp

import app.cash.turbine.test
import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.usecase.transaction.CreateTransactionUseCase
import com.example.financeapp.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.example.financeapp.domain.usecase.transaction.UpdateTransactionUseCase
import com.example.financeapp.presentation.transactions.TransactionsState
import com.example.financeapp.presentation.transactions.TransactionsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class TransactionsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getTransactionsUseCase: GetTransactionsUseCase = mock()
    private val createTransactionUseCase: CreateTransactionUseCase = mock()
    private val updateTransactionUseCase: UpdateTransactionUseCase = mock()
    private val deleteTransactionUseCase: DeleteTransactionUseCase = mock()

    private lateinit var viewModel: TransactionsViewModel

    private fun makeTransaction(id: Int) = Transaction(
        id = id, categoryId = 1, categoryName = "Кофе",
        amount = 200.0, type = "expense", description = null, date = 1000L
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init загружает транзакции и переходит в Success`() = runTest {
        val list = listOf(makeTransaction(1), makeTransaction(2))
        whenever(getTransactionsUseCase()).thenReturn(Result.success(list))

        viewModel = TransactionsViewModel(
            getTransactionsUseCase, createTransactionUseCase,
            updateTransactionUseCase, deleteTransactionUseCase
        )

        viewModel.state.test {
            // первый emit — Loading (из init)
            assertTrue(awaitItem() is TransactionsState.Loading)
            // после выполнения корутины — Success
            testDispatcher.scheduler.advanceUntilIdle()
            val success = awaitItem() as TransactionsState.Success
            assertEquals(list, success.transactions)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadTransactions переходит в Error при ошибке`() = runTest {
        whenever(getTransactionsUseCase()).thenReturn(
            Result.failure(RuntimeException("Server error"))
        )

        viewModel = TransactionsViewModel(
            getTransactionsUseCase, createTransactionUseCase,
            updateTransactionUseCase, deleteTransactionUseCase
        )

        viewModel.state.test {
            awaitItem()
            testDispatcher.scheduler.advanceUntilIdle()
            val error = awaitItem() as TransactionsState.Error
            assertEquals("Server error", error.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteTransaction вызывает loadTransactions после успеха`() = runTest {
        val list = listOf(makeTransaction(1))
        whenever(getTransactionsUseCase()).thenReturn(Result.success(list))
        whenever(deleteTransactionUseCase(1)).thenReturn(Result.success(Unit))

        viewModel = TransactionsViewModel(
            getTransactionsUseCase, createTransactionUseCase,
            updateTransactionUseCase, deleteTransactionUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.deleteTransaction(1)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is TransactionsState.Success)
    }
}