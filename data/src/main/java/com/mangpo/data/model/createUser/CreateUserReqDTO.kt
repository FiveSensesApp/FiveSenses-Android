package com.mangpo.data.model.createUser

data class CreateUserReqDTO(
    val alarmDate: String,
    val email: String,
    val isAlarmOn: Boolean,
    val nickname: String,
    val password: String
)