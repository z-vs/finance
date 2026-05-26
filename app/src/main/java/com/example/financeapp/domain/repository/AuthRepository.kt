package com.example.financeapp.domain.repository

import com.example.financeapp.domain.model.User

interface AuthRepository {
    suspend fun register(firebaseToken: String, email: String): Result<User>
    suspend fun login(firebaseToken: String): Result<User>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}