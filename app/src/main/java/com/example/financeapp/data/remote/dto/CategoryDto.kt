package com.example.financeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateCategoryRequest(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
)

data class UpdateCategoryRequest(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
)

data class CategoryResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
)