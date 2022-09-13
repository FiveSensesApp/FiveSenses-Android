package com.mangpo.data.datasource

import com.mangpo.data.model.authorize.AuthorizeReqDTO
import com.mangpo.data.model.authorize.AuthorizeResDTO
import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createUser.CreateUserReqDTO
import com.mangpo.data.model.createUser.CreateUserResDTO

interface AuthRemoteDataSource {
    suspend fun authorize(authorizeReqDTO: AuthorizeReqDTO): BaseResDTO<AuthorizeResDTO>
    suspend fun createUser(createUserReqDTO: CreateUserReqDTO): BaseResDTO<CreateUserResDTO?>
}