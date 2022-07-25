package com.mangpo.taste.view.model

import android.os.Parcelable
import com.mangpo.domain.model.RecordEntity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Record (
    val viewType: Int = 0,
    val record: @RawValue RecordEntity? = null
): Parcelable