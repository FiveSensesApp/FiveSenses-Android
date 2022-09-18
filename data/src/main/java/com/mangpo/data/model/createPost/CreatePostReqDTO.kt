package com.mangpo.data.model.createPost

data class CreatePostReqDTO(
    val category: String,
    val content: String?,
    val keyword: String,
    val star: Int
)