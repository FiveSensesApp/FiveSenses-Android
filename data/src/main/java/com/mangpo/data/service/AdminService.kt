package com.mangpo.data.service

import com.mangpo.data.model.base.BaseResDTO
import retrofit2.http.DELETE
import retrofit2.http.Path

interface AdminService {
    @DELETE("/api/admin/users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Int): BaseResDTO<Nothing>
}