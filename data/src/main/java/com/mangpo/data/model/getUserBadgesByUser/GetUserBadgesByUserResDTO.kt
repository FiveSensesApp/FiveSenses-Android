package com.mangpo.data.model.getUserBadgesByUser

data class GetUserBadgesByUserResDTO(
    val description: String,
    val id: String,
    val imgUrl: String,
    val isBefore: Boolean,
    val name: String,
    val reqConditionShort: String,
    val seqNum: Int
)