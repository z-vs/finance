package com.example.financeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateTransactionRequest(
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("date") val date: Long
)

data class UpdateTransactionRequest(
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("description") val description: String? = null,
    @SerializedName("date") val date: Long
)

data class TransactionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String?,
    @SerializedName("date") val date: Long
)