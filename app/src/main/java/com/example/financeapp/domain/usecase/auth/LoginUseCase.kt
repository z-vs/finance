package com.example.financeapp.domain.usecase.auth

import com.example.financeapp.domain.model.User
import com.example.financeapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(firebaseToken: String): Result<User> =
        authRepository.login(firebaseToken)
}