package com.mangpo.domain.model.getStat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountByDayEntity(
    val count: Int,
    val day: String
) : Parcelable