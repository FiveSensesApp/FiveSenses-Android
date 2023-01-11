package com.mangpo.data.service

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.validateDuplicate.ValidateDuplicateReqDTO
import retrofit2.http.*

interface NoInterceptorUserService {
    @POST("/api/users/validate-email")
    suspend fun validateEmail(@Query("email") email: String): BaseResDTO<Nothing>

    @POST("/api/users/lost-pw")
    suspend fun lostPassword(@Query("userEmail") email: String): BaseResDTO<Nothing>

    @POST("/api/users/validate-email-send-code")
    suspend fun validateEmailSendCode(@Query("email") email: String, @Query("emailCode") emailCode: Int): BaseResDTO<Nothing>

    @POST("/api/users/validate-duplicate")
    suspend fun validateDuplicate(@Body validateDuplicateReqDTO: ValidateDuplicateReqDTO): BaseResDTO<Nothing>
}