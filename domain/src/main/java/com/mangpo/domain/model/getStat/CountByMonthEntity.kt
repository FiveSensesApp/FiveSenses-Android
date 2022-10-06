package com.mangpo.domain.model.getStat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountByMonthEntity(
    val count: Int,
    val month: String
) : Parcelable