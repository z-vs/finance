package com.example.financeapp


import com.example.financeapp.domain.model.User
import com.example.financeapp.domain.repository.AuthRepository
import com.example.financeapp.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class LoginUseCaseTest {

    private val repository: AuthRepository = mock()
    private val useCase = LoginUseCase(repository)

    @Test
    fun `возвращает User при успешном логине`() = runTest {
        val user = User(id = 1, email = "test@test.com", token = "some_token")
        whenever(repository.login("firebase_token")).thenReturn(Result.success(user))

        val result = useCase("firebase_token")

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun `возвращает ошибку при неверном токене`() = runTest {
        whenever(repository.login("bad_token")).thenReturn(
            Result.failure(RuntimeException("Unauthorized"))
        )

        val result = useCase("bad_token")

        assertTrue(result.isFailure)
        assertEquals("Unauthorized", result.exceptionOrNull()?.message)
    }
}