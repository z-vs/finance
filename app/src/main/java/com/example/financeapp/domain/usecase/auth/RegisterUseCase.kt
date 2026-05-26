package com.example.financeapp.domain.usecase.auth

import com.example.financeapp.domain.model.User
import com.example.financeapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(firebaseToken: String, email: String): Result<User> =
        authRepository.register(firebaseToken, email)
}