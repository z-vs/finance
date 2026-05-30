package com.example.financeapp.data.repository

import com.example.financeapp.data.remote.api.CategoryApi
import com.example.financeapp.data.remote.dto.CreateCategoryRequest
import com.example.financeapp.data.remote.dto.UpdateCategoryRequest
import com.example.financeapp.data.remote.toUserMessage
import com.example.financeapp.domain.model.Category
import com.example.financeapp.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryApi: CategoryApi
) : CategoryRepository {

    override suspend fun getCategories(): Result<List<Category>> =
        try {
            val response = categoryApi.getCategories()
            Result.success(response.map { Category(it.id, it.name, it.type) })
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage()))
        }

    override suspend fun createCategory(
        name: String, type: String
    ): Result<Category> =
        try {
            val response = categoryApi.createCategory(CreateCategoryRequest(name, type))
            Result.success(Category(response.id, response.name, response.type))
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage()))
        }

    override suspend fun updateCategory(
        id: Int, name: String, type: String
    ): Result<Category> =
        try {
            val response = categoryApi.updateCategory(id, UpdateCategoryRequest(name, type))
            Result.success(Category(response.id, response.name, response.type))
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage()))
        }

    override suspend fun delete(id: Int): Result<Unit> =
        try {
            categoryApi.deleteCategory(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage()))
        }
}