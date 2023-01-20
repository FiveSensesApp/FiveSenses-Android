package com.mangpo.domain.model.authorizeNew

data class AuthorizeNewResEntity(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val grantType: String,
    val refreshToken: String
)