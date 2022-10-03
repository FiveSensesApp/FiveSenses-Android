package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO

interface AdminRemoteDataSource {
    suspend fun deleteUser(userId: Int): BaseResDTO<Nothing>
}