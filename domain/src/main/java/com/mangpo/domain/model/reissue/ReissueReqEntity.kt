package com.mangpo.domain.model.reissue

data class ReissueReqEntity(
    val accessToken: String,
    val grantType: String = "Bearer",
    val refreshToken: String
)