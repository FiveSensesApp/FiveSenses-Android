package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserInfo.GetUserInfoResDTO
import com.mangpo.data.service.UserService
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(private val service: UserService): BaseRemoteDataSource(), UserRemoteDataSource {
    override suspend fun getUserInfo(userIdx: Int): BaseResDTO<GetUserInfoResDTO?> {
        return callApi { service.getUserInfo(userIdx) }
    }

    override suspend fun validateEmail(email: String): BaseResDTO<Nothing> {
        return callApi { service.validateEmail(email) }
    }

    override suspend fun validateEmailSendCode(email: String, emailCode: Int): BaseResDTO<Nothing> {
        return callApi { service.validateEmailSendCode(email, emailCode) }
    }
}