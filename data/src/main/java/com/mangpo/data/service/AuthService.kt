package com.mangpo.data.service

import com.mangpo.data.model.authorize.AuthorizeReqDTO
import com.mangpo.data.model.authorize.AuthorizeResDTO
import com.mangpo.data.model.base.BaseResDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/auth/signin")
    suspend fun authorize(@Body authorizeReqDTO: AuthorizeReqDTO): BaseResDTO<AuthorizeResDTO>
}