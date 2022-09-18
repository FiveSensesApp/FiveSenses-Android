package com.mangpo.domain.model.createPost

data class CreatePostReqEntity(
    val category: String,
    val content: String?,
    val keyword: String,
    val star: Int
)