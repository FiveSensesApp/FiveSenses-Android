package com.mangpo.data.model.createPost

data class CreatePostResDTO(
    val category: String,
    val content: String?,
    val createdDate: String,
    val id: Int,
    val keyword: String,
    val modifiedDate: String,
    val star: Int
)