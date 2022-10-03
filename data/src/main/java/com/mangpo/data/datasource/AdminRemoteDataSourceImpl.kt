package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.service.AdminService
import javax.inject.Inject

class AdminRemoteDataSourceImpl @Inject constructor(private val service: AdminService): BaseRemoteDataSource(), AdminRemoteDataSource {
    override suspend fun deleteUser(userId: Int): BaseResDTO<Nothing> {
        return callApi { service.deleteUser(userId) }
    }
}