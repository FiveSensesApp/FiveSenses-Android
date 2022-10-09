package com.mangpo.data.service

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserBadgesByUser.GetUserBadgesByUserResDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface BadgeService {
    @GET("/api/users/{userId}/badges")
    suspend fun getUserBadgesByUser(@Path("userId") userId: Int): BaseResDTO<List<GetUserBadgesByUserResDTO>?>
}