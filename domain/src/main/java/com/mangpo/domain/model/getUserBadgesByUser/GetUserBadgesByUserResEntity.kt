package com.mangpo.domain.model.getUserBadgesByUser

data class GetUserBadgesByUserResEntity(
    val description: String,
    val id: String,
    val imgUrl: String,
    val isBefore: Boolean,
    val name: String,
    val reqConditionShort: String,
    val seqNum: Int
)