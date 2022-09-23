package com.mangpo.data.model.updatePost

data class UpdatePostResDTO(
    val category: String,
    val content: String?,
    val createdDate: String,
    val id: Int,
    val keyword: String,
    val modifiedDate: String,
    val star: Int
)