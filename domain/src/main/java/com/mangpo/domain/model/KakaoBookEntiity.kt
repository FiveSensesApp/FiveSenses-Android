package com.mangpo.domain.model

data class KakaoBookEntiity(
    val bookName: String,
    val isbn: String,
    val author: List<String>,
    val thumbnail: String
)
