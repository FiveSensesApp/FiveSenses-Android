package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.domain.model.getStat.GetStatResEntity
import com.mangpo.data.service.StatService
import javax.inject.Inject

class StatRemoteDataSourceImpl @Inject constructor(private val service: StatService): BaseRemoteDataSource(), StatRemoteDataSource {
    override suspend fun getStat(userId: Int): BaseResDTO<GetStatResEntity> {
        return callApi { service.getStat(userId) }
    }
}