package com.mangpo.data.service

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserBadgesByUser.GetUserBadgesByUserResDTO
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BadgeService {
    @POST("/api/badges/check-updates")
    suspend fun checkUpdates()

    @POST("/api/badges/check-thanks")
    suspend fun checkThanks(): BaseResDTO<GetUserBadgesByUserResDTO?>

    @GET("/api/users/{userId}/badges")
    suspend fun getUserBadgesByUser(@Path("userId") userId: Int): BaseResDTO<List<GetUserBadgesByUserResDTO>?>
}