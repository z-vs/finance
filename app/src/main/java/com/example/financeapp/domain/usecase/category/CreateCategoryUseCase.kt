package com.example.financeapp.domain.usecase.category

import com.example.financeapp.domain.model.Category
import com.example.financeapp.domain.repository.CategoryRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(
        name: String,
        type: String
    ): Result<Category> = categoryRepository.createCategory(name, type)
}