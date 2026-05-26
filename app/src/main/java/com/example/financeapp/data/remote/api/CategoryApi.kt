package com.example.financeapp.data.remote.api

import com.example.financeapp.data.remote.dto.CategoryResponse
import com.example.financeapp.data.remote.dto.CreateCategoryRequest
import com.example.financeapp.data.remote.dto.UpdateCategoryRequest
import retrofit2.http.*

interface CategoryApi {
    @GET("categories")
    suspend fun getCategories(): List<CategoryResponse>

    @POST("categories")
    suspend fun createCategory(@Body request: CreateCategoryRequest): CategoryResponse

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body request: UpdateCategoryRequest
    ): CategoryResponse

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Int)
}