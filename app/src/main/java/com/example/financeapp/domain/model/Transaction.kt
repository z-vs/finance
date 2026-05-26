package com.example.financeapp.domain.model

data class Transaction(
    val id: Int,
    val categoryId: Int,
    val categoryName: String,
    val amount: Double,
    val type: String,
    val description: String?,
    val date: Long
)