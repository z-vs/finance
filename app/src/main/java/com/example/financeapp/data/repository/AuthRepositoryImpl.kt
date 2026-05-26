package com.example.financeapp.data.repository

import com.example.financeapp.data.local.TokenStorage
import com.example.financeapp.data.remote.api.AuthApi
import com.example.financeapp.data.remote.dto.LoginRequest
import com.example.financeapp.data.remote.dto.RegisterRequest
import com.example.financeapp.domain.model.User
import com.example.financeapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun register(firebaseToken: String, email: String): Result<User> =
        try {
            val response = authApi.register(RegisterRequest(firebaseToken, email))
            tokenStorage.saveToken(response.token)
            Result.success(User(response.userId, response.email, response.token))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun login(firebaseToken: String): Result<User> =
        try {
            val response = authApi.login(LoginRequest(firebaseToken))
            tokenStorage.saveToken(response.token)
            Result.success(User(response.userId, response.email, response.token))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun logout() {
        tokenStorage.clearToken()
    }

    override suspend fun isLoggedIn(): Boolean =
        tokenStorage.token.firstOrNull() != null
}