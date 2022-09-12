package com.mangpo.data.model.getUserInfo

data class GetUserInfoResDTO(
    val alarmDate: String,
    val createdDate: String,
    val email: String,
    val emailValidCode: Any,
    val id: Int,
    val isAlarmOn: Boolean,
    val modifiedDate: String,
    val nickname: String
)