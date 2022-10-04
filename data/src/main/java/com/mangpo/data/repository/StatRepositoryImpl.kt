package com.mangpo.data.repository

import com.mangpo.data.datasource.StatRemoteDataSource
import com.mangpo.data.mapper.StatMapper
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getStat.GetStatResEntity
import com.mangpo.domain.repository.StatRepository
import javax.inject.Inject

class StatRepositoryImpl @Inject constructor(private val dataSource: StatRemoteDataSource): BaseRepository(), StatRepository {
    override suspend fun getStat(userId: Int): BaseResEntity<GetStatResEntity> {
        val response = dataSource.getStat(userId)

        return sendData(response) { StatMapper.mapperToGetStatResEntity(response) }
    }
}