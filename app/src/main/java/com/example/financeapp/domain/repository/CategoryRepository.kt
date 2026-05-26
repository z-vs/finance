package com.example.financeapp.domain.repository

import com.example.financeapp.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun createCategory(name: String, type: String, icon: String?): Result<Category>
    suspend fun updateCategory(id: Int, name: String, type: String): Result<Category>
    suspend fun delete(id: Int): Result<Unit>
}