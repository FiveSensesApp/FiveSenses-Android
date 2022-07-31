package com.mangpo.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecordEntity(
    val recordId: Int,
    val taste: Int,
    val keyword: String,
    val content: String? = null,
    val date: String,
    val star: Float
) : Parcelable
