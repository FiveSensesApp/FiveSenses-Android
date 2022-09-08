package com.mangpo.data.repository

import com.mangpo.data.datasource.AuthRemoteDataSource
import com.mangpo.data.mapper.AuthMapper
import com.mangpo.domain.model.authorize.AuthorizeReqEntity
import com.mangpo.domain.model.authorize.AuthorizeResEntity
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.AuthRepository

class AuthRepository(private val dataSource: AuthRemoteDataSource): BaseRepository(), AuthRepository {
    override suspend fun authorize(authorizeReqEntity: AuthorizeReqEntity): BaseResEntity<AuthorizeResEntity> {
        val data = dataSource.authorize(authorizeReqEntity)

        return sendData(data) { AuthMapper.mapperToAuthorizeResEntity(data) }
    }
}