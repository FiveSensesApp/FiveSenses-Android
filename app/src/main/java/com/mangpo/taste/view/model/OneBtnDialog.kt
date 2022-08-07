package com.mangpo.taste.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OneBtnDialog(
    val title: String,
    val msg: String,
    val action: String,
    val actionBtnPadding: List<Int>
): Parcelable
