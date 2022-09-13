package com.mangpo.domain.repository

import com.mangpo.domain.model.authorize.AuthorizeReqEntity
import com.mangpo.domain.model.authorize.AuthorizeResEntity
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.domain.model.createUser.CreateUserResEntity

interface AuthRepository {
    suspend fun authorize(authorizeReqEntity: AuthorizeReqEntity): BaseResEntity<AuthorizeResEntity>
    suspend fun createUser(createUserReqEntity: CreateUserReqEntity): BaseResEntity<CreateUserResEntity?>
}