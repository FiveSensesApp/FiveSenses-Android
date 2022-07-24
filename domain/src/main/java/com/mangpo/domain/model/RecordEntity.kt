package com.mangpo.domain.model

data class RecordEntity(
    val taste: Int,
    val keyword: String,
    val content: String? = null,
    val date: String,
    val star: Float
)
