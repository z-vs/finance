package com.example.financeapp.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.domain.model.Category
import com.example.financeapp.domain.repository.CategoryRepository
import com.example.financeapp.domain.usecase.category.CreateCategoryUseCase
import com.example.financeapp.domain.usecase.category.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CategoriesState {
    object Loading : CategoriesState()
    data class Success(val categories: List<Category>) : CategoriesState()
    data class Error(val message: String) : CategoriesState()
}

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CategoriesState>(CategoriesState.Loading)
    val state: StateFlow<CategoriesState> = _state

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _state.value = CategoriesState.Loading
            getCategoriesUseCase()
                .onSuccess { _state.value = CategoriesState.Success(it) }
                .onFailure { _state.value = CategoriesState.Error(it.message ?: "Error") }
        }
    }

    fun createCategory(name: String, type: String) {
        viewModelScope.launch {
            createCategoryUseCase(name, type)
                .onSuccess { loadCategories() }
                .onFailure { _state.value = CategoriesState.Error(it.message ?: "Error") }
        }
    }

    fun updateCategory(id: Int, name: String, type: String) {
        viewModelScope.launch {
            categoryRepository.updateCategory(id, name, type)
                .onSuccess { loadCategories() }
                .onFailure { _state.value = CategoriesState.Error(it.message ?: "Error") }
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            categoryRepository.delete(id)
                .onSuccess { loadCategories() }
                .onFailure { _state.value = CategoriesState.Error(it.message ?: "Error") }
        }
    }
}