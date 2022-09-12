package com.mangpo.data.service

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserInfo.GetUserInfoResDTO
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("/api/users/{userIdx}")
    suspend fun getUserInfo(@Path("userIdx") userIdx: Int): BaseResDTO<GetUserInfoResDTO?>

    @POST("/api/users/validate-email")
    suspend fun validateEmail(@Query("email") email: String): BaseResDTO<Nothing>

    @POST("/api/users/lost-pw")
    suspend fun lostPassword(@Query("userEmail") email: String): BaseResDTO<Nothing>

    @POST("/api/users/validate-email-send-code")
    suspend fun validateEmailSendCode(@Query("email") email: String, @Query("emailCode") emailCode: Int): BaseResDTO<Nothing>
}