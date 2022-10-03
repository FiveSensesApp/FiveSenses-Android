package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserInfo.GetUserInfoResDTO
import com.mangpo.data.model.updateUser.UpdateUserReqDTO
import com.mangpo.data.model.validateDuplicate.ValidateDuplicateReqDTO

interface UserRemoteDataSource {
    suspend fun getUserInfo(userIdx: Int): BaseResDTO<GetUserInfoResDTO?>
    suspend fun validateEmail(email: String): BaseResDTO<Nothing>
    suspend fun validateEmailSendCode(email: String, emailCode: Int): BaseResDTO<Nothing>
    suspend fun validateDuplicate(validateDuplicateReqDTO: ValidateDuplicateReqDTO): BaseResDTO<Nothing>
    suspend fun lostPassword(email: String): BaseResDTO<Nothing>
    suspend fun updateUser(updateUserReqDTO: UpdateUserReqDTO): BaseResDTO<Nothing>
}