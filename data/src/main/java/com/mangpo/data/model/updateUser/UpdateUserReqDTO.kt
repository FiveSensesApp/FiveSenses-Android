package com.mangpo.data.model.updateUser

data class UpdateUserReqDTO(
    val alarmDate: String,
    val isAlarmOn: Boolean,
    val nickname: String,
    val userId: Int
)