package com.mangpo.data.repository

import com.mangpo.data.datasource.AuthRemoteDataSource
import com.mangpo.data.mapper.AuthMapper
import com.mangpo.domain.model.authorize.AuthorizeReqEntity
import com.mangpo.domain.model.authorize.AuthorizeResEntity
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val dataSource: AuthRemoteDataSource): BaseRepository(), AuthRepository {
    override suspend fun authorize(authorizeReqEntity: AuthorizeReqEntity): BaseResEntity<AuthorizeResEntity> {
        val response = dataSource.authorize(AuthMapper.mapperToAuthorizeReqDTO(authorizeReqEntity))

        return sendData(response) { AuthMapper.mapperToAuthorizeResEntity(response) }
    }
}