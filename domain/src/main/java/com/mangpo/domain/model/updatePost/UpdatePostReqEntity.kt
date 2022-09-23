package com.mangpo.domain.model.updatePost

data class UpdatePostReqEntity(
    val postId: Int,
    val category: String,
    val content: String?,
    val keyword: String,
    val star: Int
)