package com.mangpo.data.repository

import com.mangpo.data.datasource.AdminRemoteDataSource
import com.mangpo.data.mapper.BaseMapper
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.AdminRepository
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(private val dataSource: AdminRemoteDataSource): BaseRepository(), AdminRepository {
    override suspend fun deleteUser(userId: Int): BaseResEntity<Nothing> {
        val response = dataSource.deleteUser(userId)

        return sendData(response) { BaseMapper.mapperToBaseResEntity(response) }
    }
}