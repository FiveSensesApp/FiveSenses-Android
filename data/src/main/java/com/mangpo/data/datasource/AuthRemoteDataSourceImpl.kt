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
import com.mangpo.data.service.AuthService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val service: AuthService): BaseRemoteDataSource(), AuthRemoteDataSource {
    override suspend fun authorize(authorizeReqDTO: AuthorizeReqDTO): BaseResDTO<AuthorizeResDTO> {
        return callApi { service.authorize(authorizeReqDTO) }
    }

    override suspend fun authorizeNew(authorizeNewReqDTO: AuthorizeNewReqDTO): BaseResDTO<AuthorizeNewResDTO?> {
        return callApi { service.authorizeNew(authorizeNewReqDTO) }
    }

    override suspend fun createUser(createUserReqDTO: CreateUserReqDTO): BaseResDTO<CreateUserResDTO?> {
        return callApi { service.createUser(createUserReqDTO) }
    }

    override suspend fun reissue(reissueReqDTO: ReissueReqDTO): BaseResDTO<ReissueResDTO?> {
        return callApi { service.reissue(reissueReqDTO) }
    }
}