package com.mangpo.data.service

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.changePassword.ChangePasswordReqDTO
import com.mangpo.data.model.getUserInfo.GetUserInfoResDTO
import com.mangpo.data.model.updateUser.UpdateUserReqDTO
import retrofit2.http.*

interface UserService {
    @GET("/api/users/{userIdx}")
    suspend fun getUserInfo(@Path("userIdx") userIdx: Int): BaseResDTO<GetUserInfoResDTO?>

    @POST("/api/users/change-pw")
    suspend fun changePassword(@Body changePasswordReqDTO: ChangePasswordReqDTO): BaseResDTO<Nothing>

    @PUT("/api/users/{userId}")
    suspend fun updateUser(@Path("userId") userId: Int, @Body updateUserReqDTO: UpdateUserReqDTO): BaseResDTO<Nothing>
}