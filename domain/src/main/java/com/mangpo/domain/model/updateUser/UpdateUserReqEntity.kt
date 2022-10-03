package com.mangpo.domain.model.updateUser

data class UpdateUserReqEntity(
    val alarmDate: String,
    val isAlarmOn: Boolean,
    val nickname: String,
    val userId: Int
)