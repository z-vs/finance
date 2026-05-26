package com.example.financeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateCategoryRequest(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("icon") val icon: String? = null
)

data class UpdateCategoryRequest(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("icon") val icon: String? = null
)

data class CategoryResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("icon") val icon: String?
)