package com.mangpo.data.datasource

import com.mangpo.data.model.authorize.AuthorizeReqDTO
import com.mangpo.data.model.authorize.AuthorizeResDTO
import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createUser.CreateUserReqDTO
import com.mangpo.data.model.createUser.CreateUserResDTO
import com.mangpo.data.service.AuthService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val service: AuthService): BaseRemoteDataSource(), AuthRemoteDataSource {
    override suspend fun authorize(authorizeReqDTO: AuthorizeReqDTO): BaseResDTO<AuthorizeResDTO> {
        return callApi { service.authorize(authorizeReqDTO) }
    }

    override suspend fun createUser(createUserReqDTO: CreateUserReqDTO): BaseResDTO<CreateUserResDTO?> {
        return callApi { service.createUser(createUserReqDTO) }
    }
}