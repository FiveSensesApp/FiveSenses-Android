package com.mangpo.data.datasource

import com.mangpo.data.model.authorize.AuthorizeReqDTO
import com.mangpo.data.model.authorize.AuthorizeResDTO
import com.mangpo.data.model.authorizeNew.AuthorizeNewReqDTO
import com.mangpo.data.model.authorizeNew.AuthorizeNewResDTO
import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createUser.CreateUserReqDTO
import com.mangpo.data.model.createUser.CreateUserResDTO
import com.mangpo.data.model.reissue.ReissueReqDTO
import com.mangpo.data.model.reissue.ReissueResDTO

interface AuthRemoteDataSource {
    suspend fun authorize(authorizeReqDTO: AuthorizeReqDTO): BaseResDTO<AuthorizeResDTO>
    suspend fun authorizeNew(authorizeNewReqDTO: AuthorizeNewReqDTO): BaseResDTO<AuthorizeNewResDTO?>
    suspend fun createUser(createUserReqDTO: CreateUserReqDTO): BaseResDTO<CreateUserResDTO?>
    suspend fun reissue(reissueReqDTO: ReissueReqDTO): BaseResDTO<ReissueResDTO?>
}