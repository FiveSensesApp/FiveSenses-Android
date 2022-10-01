package com.mangpo.taste.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TwoBtnDialog (
    val title: String,
    val msg: String,
    val leftAction: String,
    val rightAction: String,
    val background: Int?
): Parcelable