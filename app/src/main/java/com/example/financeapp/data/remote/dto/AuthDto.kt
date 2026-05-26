package com.example.financeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("firebaseToken") val firebaseToken: String,
    @SerializedName("email") val email: String
)

data class LoginRequest(
    @SerializedName("firebaseToken") val firebaseToken: String
)

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("email") val email: String
)