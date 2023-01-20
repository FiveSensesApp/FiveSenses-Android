package com.mangpo.data.model.authorizeNew

data class AuthorizeNewResDTO(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val grantType: String,
    val refreshToken: String
)