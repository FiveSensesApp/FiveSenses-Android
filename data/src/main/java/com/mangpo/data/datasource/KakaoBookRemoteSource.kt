package com.mangpo.data.datasource

import android.util.Log
import com.mangpo.data.KakaoBookMapper
import com.mangpo.data.model.KakaoBookRes
import com.mangpo.domain.model.NetworkResult
import com.mangpo.data.service.KakaoBookService
import com.mangpo.domain.model.KakaoBookEntiity
import java.io.IOException
import javax.inject.Inject

class KakaoBookRemoteSourceImpl @Inject constructor(private val service: KakaoBookService): BaseRemoteDataSource(), KakaoBookRemoteDataSource {
    override suspend fun getBooksByName(name: String): NetworkResult<KakaoBookRes> {
        return toNetworkResult { service.searchBooks(name, "title") }
        /*try {
            return KakaoBookMapper.mapperToKakaoBook(service.searchBooks(name, "title").documents)
        } catch (e: IOException) {
            Log.d("KakaoBookRemoteSourceImpl", "IOException")
            return listOf()
        }*/
    }
}