package com.mangpo.domain.repository

import com.mangpo.domain.model.KakaoBookEntiity
import com.mangpo.domain.model.NetworkResult

interface KakaoBookRepository {
    suspend fun getBooksByName(name: String): NetworkResult<List<KakaoBookEntiity>>
}