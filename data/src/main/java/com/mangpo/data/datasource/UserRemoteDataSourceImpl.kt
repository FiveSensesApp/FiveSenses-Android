package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserInfo.GetUserInfoResDTO
import com.mangpo.data.model.updateUser.UpdateUserReqDTO
import com.mangpo.data.model.validateDuplicate.ValidateDuplicateReqDTO
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

    override suspend fun validateDuplicate(validateDuplicateReqDTO: ValidateDuplicateReqDTO): BaseResDTO<Nothing> {
        return callApi { service.validateDuplicate(validateDuplicateReqDTO) }
    }

    override suspend fun lostPassword(email: String): BaseResDTO<Nothing> {
        return callApi { service.lostPassword(email) }
    }

    override suspend fun updateUser(updateUserReqDTO: UpdateUserReqDTO): BaseResDTO<Nothing> {
        return callApi { service.updateUser(updateUserReqDTO.userId, updateUserReqDTO) }
    }
}