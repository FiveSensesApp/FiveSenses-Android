package com.mangpo.data.repository

import com.mangpo.data.KakaoBookMapper
import com.mangpo.data.datasource.KakaoBookRemoteDataSource
import com.mangpo.domain.model.KakaoBookEntiity
import com.mangpo.domain.model.NetworkResult
import com.mangpo.domain.repository.KakaoBookRepository
import javax.inject.Inject

class KakaoBookRepositoryImpl @Inject constructor(private val dataSource: KakaoBookRemoteDataSource): BaseRepository(), KakaoBookRepository {
    override suspend fun getBooksByName(name: String): NetworkResult<List<KakaoBookEntiity>> {
        return dataSource.getBooksByName(name).mapNetworkResult {
            KakaoBookMapper.mapperToKakaoBook(it.documents)
        }
    }
}