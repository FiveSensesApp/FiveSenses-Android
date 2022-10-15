package com.mangpo.domain.model.getUserBadgesByUser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetUserBadgesByUserResEntity(
    val description: String,
    val id: String,
    val imgUrl: String,
    val isBefore: Boolean,
    val name: String,
    val reqConditionShort: String,
    val seqNum: Int
) : Parcelable