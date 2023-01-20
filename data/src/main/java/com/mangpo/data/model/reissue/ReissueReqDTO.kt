package com.mangpo.data.model.reissue

data class ReissueReqDTO(
    val accessToken: String,
    val grantType: String = "Bearer",
    val refreshToken: String
)