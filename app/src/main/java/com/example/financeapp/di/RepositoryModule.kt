package com.example.financeapp.di

import com.example.financeapp.data.repository.AuthRepositoryImpl
import com.example.financeapp.data.repository.CategoryRepositoryImpl
import com.example.financeapp.data.repository.TransactionRepositoryImpl
import com.example.financeapp.domain.repository.AuthRepository
import com.example.financeapp.domain.repository.CategoryRepository
import com.example.financeapp.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
}