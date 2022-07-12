package com.mangpo.data

import com.mangpo.data.model.KakaoBook
import com.mangpo.domain.model.KakaoBookEntiity

object KakaoBookMapper {
    fun mapperToKakaoBook(books: List<KakaoBook>): List<KakaoBookEntiity> = books.toList().map {
        KakaoBookEntiity(
            it.title,
            it.isbn,
            it.authors,
            it.thumbnail
        )
    }
}