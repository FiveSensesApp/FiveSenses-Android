package com.mangpo.domain.repository

import com.mangpo.domain.model.authorize.AuthorizeReqEntity
import com.mangpo.domain.model.authorize.AuthorizeResEntity
import com.mangpo.domain.model.authorizeNew.AuthorizeNewReqEntity
import com.mangpo.domain.model.authorizeNew.AuthorizeNewResEntity
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.domain.model.createUser.CreateUserResEntity
import com.mangpo.domain.model.reissue.ReissueReqEntity
import com.mangpo.domain.model.reissue.ReissueResEntity

interface AuthRepository {
    suspend fun authorize(authorizeReqEntity: AuthorizeReqEntity): BaseResEntity<AuthorizeResEntity>
    suspend fun authorizeNew(authorizeNewReqEntity: AuthorizeNewReqEntity): BaseResEntity<AuthorizeNewResEntity?>
    suspend fun createUser(createUserReqEntity: CreateUserReqEntity): BaseResEntity<CreateUserResEntity?>
    suspend fun reissue(reissueReqEntity: ReissueReqEntity): BaseResEntity<ReissueResEntity?>
}