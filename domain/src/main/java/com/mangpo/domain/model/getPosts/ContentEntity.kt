package com.mangpo.domain.model.getPosts

data class ContentEntity(
    val id: Int,
    val category: String,
    val keyword: String,
    val star: Int,
    val content: String?,
    val createdDate: String
)
