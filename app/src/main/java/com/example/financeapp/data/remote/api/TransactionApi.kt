package com.example.financeapp.data.remote.api

import com.example.financeapp.data.remote.dto.CreateTransactionRequest
import com.example.financeapp.data.remote.dto.TransactionResponse
import com.example.financeapp.data.remote.dto.UpdateTransactionRequest
import retrofit2.http.*

interface TransactionApi {
    @GET("transactions")
    suspend fun getTransactions(): List<TransactionResponse>

    @POST("transactions")
    suspend fun createTransaction(@Body request: CreateTransactionRequest): TransactionResponse

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Int,
        @Body request: UpdateTransactionRequest
    ): TransactionResponse

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Int)
}