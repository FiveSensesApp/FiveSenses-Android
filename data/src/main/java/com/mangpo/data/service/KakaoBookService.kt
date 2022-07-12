package com.mangpo.data.service

import com.mangpo.data.BuildConfig
import com.mangpo.data.model.KakaoBookRes
import com.mangpo.data.model.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoBookService {
    @Headers("Authorization: ${BuildConfig.KAKAOBOOK_KEY}")
    @GET("book?size=50")
    suspend fun searchBooks(@Query("query") query: String, @Query("target") target: String): KakaoBookRes
}