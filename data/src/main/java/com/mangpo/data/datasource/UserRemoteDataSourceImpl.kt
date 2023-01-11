package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.changePassword.ChangePasswordReqDTO
import com.mangpo.data.model.getUserInfo.GetUserInfoResDTO
import com.mangpo.data.model.updateUser.UpdateUserReqDTO
import com.mangpo.data.model.validateDuplicate.ValidateDuplicateReqDTO
import com.mangpo.data.service.NoInterceptorUserService
import com.mangpo.data.service.UserService
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(private val userService: UserService, private val noInterceptorUserService: NoInterceptorUserService): BaseRemoteDataSource(), UserRemoteDataSource {
    override suspend fun getUserInfo(userIdx: Int): BaseResDTO<GetUserInfoResDTO?> {
        return callApi { userService.getUserInfo(userIdx) }
    }

    override suspend fun validateEmail(email: String): BaseResDTO<Nothing> {
        return callApi { noInterceptorUserService.validateEmail(email) }
    }

    override suspend fun validateEmailSendCode(email: String, emailCode: Int): BaseResDTO<Nothing> {
        return callApi { noInterceptorUserService.validateEmailSendCode(email, emailCode) }
    }

    override suspend fun validateDuplicate(validateDuplicateReqDTO: ValidateDuplicateReqDTO): BaseResDTO<Nothing> {
        return callApi { noInterceptorUserService.validateDuplicate(validateDuplicateReqDTO) }
    }

    override suspend fun lostPassword(email: String): BaseResDTO<Nothing> {
        return callApi { noInterceptorUserService.lostPassword(email) }
    }

    override suspend fun updateUser(updateUserReqDTO: UpdateUserReqDTO): BaseResDTO<Nothing> {
        return callApi { userService.updateUser(updateUserReqDTO.userId, updateUserReqDTO) }
    }

    override suspend fun changePassword(changePasswordReqDTO: ChangePasswordReqDTO): BaseResDTO<Nothing> {
        return callApi { userService.changePassword(changePasswordReqDTO) }
    }
}