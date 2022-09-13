package com.mangpo.domain.model.createUser

data class CreateUserResEntity(
    val alarmDate: String,
    val startDayCnt: Int,
    val email: String,
    val id: Int,
    val isAlarmOn: Boolean,
    val nickname: String
)