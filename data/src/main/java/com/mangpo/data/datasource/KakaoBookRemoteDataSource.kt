package com.mangpo.data.datasource

import com.mangpo.data.model.KakaoBookRes
import com.mangpo.domain.model.NetworkResult

interface KakaoBookRemoteDataSource {
    suspend fun getBooksByName(name: String): NetworkResult<KakaoBookRes>
}