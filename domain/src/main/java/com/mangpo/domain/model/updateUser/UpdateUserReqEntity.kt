package com.mangpo.domain.model.updateUser

data class UpdateUserReqEntity(
    val alarmDate: String? = null,
    val isAlarmOn: Boolean? = null,
    val nickname: String? = null,
    val badgeRepresent: String? = null,
    val userId: Int
)