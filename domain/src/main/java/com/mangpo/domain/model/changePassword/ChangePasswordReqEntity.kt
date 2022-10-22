package com.mangpo.domain.model.changePassword

data class ChangePasswordReqEntity(
    val newPw: String,
    val ogPw: String
)