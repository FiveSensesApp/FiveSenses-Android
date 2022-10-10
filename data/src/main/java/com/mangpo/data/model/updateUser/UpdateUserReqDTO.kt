package com.mangpo.data.model.updateUser

data class UpdateUserReqDTO(
    val alarmDate: String? = null,
    val isAlarmOn: Boolean? = null,
    val nickname: String? = null,
    val userId: Int
)