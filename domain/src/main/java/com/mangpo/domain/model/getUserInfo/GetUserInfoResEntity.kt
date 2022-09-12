package com.mangpo.domain.model.getUserInfo

data class GetUserInfoResEntity(
    val userIdx: Int,
    val nickname: String,
    val email: String,
    val startDayCnt: Int,
    val isAlarmOn: Boolean,
    val alarmTime: String
)