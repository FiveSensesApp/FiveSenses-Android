package com.mangpo.data.service

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getStat.GetStatResDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface StatService {
    @GET("/api/stats")
    suspend fun getStat(@Query("userId") userId: Int): BaseResDTO<GetStatResDTO>
}