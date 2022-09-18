package com.mangpo.domain.repository

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getUserInfo.GetUserInfoResEntity

interface UserRepository {
    suspend fun getUserInfo(userIdx: Int): BaseResEntity<GetUserInfoResEntity?>
    suspend fun validateEmail(email: String): BaseResEntity<Nothing>
    suspend fun validateEmailSendCode(email: String, emailCode: Int): BaseResEntity<Nothing>
    suspend fun validateDuplicate(email: String): BaseResEntity<Nothing>
    suspend fun lostPassword(email: String): BaseResEntity<Nothing>
}