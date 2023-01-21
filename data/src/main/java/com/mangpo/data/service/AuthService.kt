package com.mangpo.data.service

import com.mangpo.data.model.authorize.AuthorizeReqDTO
import com.mangpo.data.model.authorize.AuthorizeResDTO
import com.mangpo.data.model.authorizeNew.AuthorizeNewReqDTO
import com.mangpo.data.model.authorizeNew.AuthorizeNewResDTO
import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createUser.CreateUserReqDTO
import com.mangpo.data.model.createUser.CreateUserResDTO
import com.mangpo.data.model.reissue.ReissueReqDTO
import com.mangpo.data.model.reissue.ReissueResDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/auth/signin")
    suspend fun authorize(@Body authorizeReqDTO: AuthorizeReqDTO): BaseResDTO<AuthorizeResDTO>

    @POST("/api/auth/login")
    suspend fun authorizeNew(@Body authorizeNewReqDTO: AuthorizeNewReqDTO): BaseResDTO<AuthorizeNewResDTO?>

    @POST("/api/auth/signup")
    suspend fun createUser(@Body createUserReqDTO: CreateUserReqDTO): BaseResDTO<CreateUserResDTO?>

    @POST("/api/auth/reissue")
    suspend fun reissue(@Body reissueReqDTO: ReissueReqDTO): BaseResDTO<ReissueResDTO?>
}