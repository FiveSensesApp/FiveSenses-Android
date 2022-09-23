package com.mangpo.data.model.updatePost

data class UpdatePostReqDTO(
    val category: String,
    val content: String?,
    val keyword: String,
    val star: Int
)