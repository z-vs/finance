package com.example.financeapp.di

import com.example.financeapp.domain.repository.AuthRepository
import com.example.financeapp.domain.repository.CategoryRepository
import com.example.financeapp.domain.repository.TransactionRepository
import com.example.financeapp.domain.usecase.auth.LoginUseCase
import com.example.financeapp.domain.usecase.auth.RegisterUseCase
import com.example.financeapp.domain.usecase.category.CreateCategoryUseCase
import com.example.financeapp.domain.usecase.category.GetCategoriesUseCase
import com.example.financeapp.domain.usecase.transaction.CreateTransactionUseCase
import com.example.financeapp.domain.usecase.transaction.DeleteTransactionUseCase
import com.example.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.example.financeapp.domain.usecase.transaction.UpdateTransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase =
        LoginUseCase(authRepository)

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase =
        RegisterUseCase(authRepository)

    @Provides
    @Singleton
    fun provideGetTransactionsUseCase(transactionRepository: TransactionRepository): GetTransactionsUseCase =
        GetTransactionsUseCase(transactionRepository)

    @Provides
    @Singleton
    fun provideCreateTransactionUseCase(transactionRepository: TransactionRepository): CreateTransactionUseCase =
        CreateTransactionUseCase(transactionRepository)

    @Provides
    @Singleton
    fun provideUpdateTransactionUseCase(transactionRepository: TransactionRepository): UpdateTransactionUseCase =
        UpdateTransactionUseCase(transactionRepository)

    @Provides
    @Singleton
    fun provideDeleteTransactionUseCase(transactionRepository: TransactionRepository): DeleteTransactionUseCase =
        DeleteTransactionUseCase(transactionRepository)

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(categoryRepository: CategoryRepository): GetCategoriesUseCase =
        GetCategoriesUseCase(categoryRepository)

    @Provides
    @Singleton
    fun provideCreateCategoryUseCase(categoryRepository: CategoryRepository): CreateCategoryUseCase =
        CreateCategoryUseCase(categoryRepository)
}