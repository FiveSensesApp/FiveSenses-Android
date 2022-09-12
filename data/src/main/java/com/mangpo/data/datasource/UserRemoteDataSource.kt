package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserInfo.GetUserInfoResDTO

interface UserRemoteDataSource {
    suspend fun getUserInfo(userIdx: Int): BaseResDTO<GetUserInfoResDTO?>
    suspend fun validateEmail(email: String): BaseResDTO<Nothing>
    suspend fun validateEmailSendCode(email: String, emailCode: Int): BaseResDTO<Nothing>
}