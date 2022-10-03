package com.mangpo.data.model.createUser

data class CreateUserResDTO(
    val alarmDate: String,
    val createdDate: String,
    val email: String,
    val emailValidCode: Int,
    val id: Int,
    val isAlarmOn: Boolean,
    val modifiedDate: String,
    val nickname: String,
    val isMarketingAllowed: Boolean
)