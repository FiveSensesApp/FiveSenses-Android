package com.mangpo.data.model.reissue

data class ReissueResDTO(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val grantType: String,
    val refreshToken: String
)