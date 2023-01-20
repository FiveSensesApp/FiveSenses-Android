package com.mangpo.domain.model.reissue

data class ReissueResEntity(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val grantType: String,
    val refreshToken: String
)