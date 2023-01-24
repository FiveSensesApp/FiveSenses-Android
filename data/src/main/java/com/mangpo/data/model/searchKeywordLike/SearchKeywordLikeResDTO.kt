package com.mangpo.data.model.searchKeywordLike

data class SearchKeywordLikeResDTO(
    val category: String,
    val content: String,
    val createdDate: String,
    val id: Int,
    val keyword: String,
    val modifiedDate: String,
    val star: Int
)