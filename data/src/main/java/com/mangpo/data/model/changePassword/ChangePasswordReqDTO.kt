package com.mangpo.data.model.changePassword

data class ChangePasswordReqDTO(
    val newPw: String,
    val ogPw: String
)