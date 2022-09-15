package com.mangpo.data.repository

import com.mangpo.data.datasource.UserRemoteDataSource
import com.mangpo.data.mapper.BaseMapper
import com.mangpo.data.mapper.UserMapper
import com.mangpo.data.model.validateDuplicate.ValidateDuplicateReqDTO
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getUserInfo.GetUserInfoResEntity
import com.mangpo.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userRemoteDataSource: UserRemoteDataSource): BaseRepository(), UserRepository {
    override suspend fun getUserInfo(userIdx: Int): BaseResEntity<GetUserInfoResEntity?> {
        val response = userRemoteDataSource.getUserInfo(userIdx)

        return sendData(response) { UserMapper.mapperToGetUserInfoEntity(response) }
    }

    override suspend fun validateEmail(email: String): BaseResEntity<Nothing> {
        val response = userRemoteDataSource.validateEmail(email)

        return sendData(response) { BaseMapper.mapperToBaseResEntity(response) }
    }

    override suspend fun validateEmailSendCode(
        email: String,
        emailCode: Int
    ): BaseResEntity<Nothing> {
        val response = userRemoteDataSource.validateEmailSendCode(email, emailCode)

        return sendData(response) { BaseMapper.mapperToBaseResEntity(response) }
    }

    override suspend fun validateDuplicate(email: String): BaseResEntity<Nothing> {
        val response = userRemoteDataSource.validateDuplicate(ValidateDuplicateReqDTO(email))

        return sendData(response) { BaseMapper.mapperToBaseResEntity(response) }
    }
}