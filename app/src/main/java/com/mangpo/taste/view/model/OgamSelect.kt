package com.mangpo.taste.view.model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class OgamSelect(
    val character: @RawValue Drawable? = null,
    val sense1: String = "",
    val sense2: String = "",
    val senseTextColor: Int = -1,
    val emptyStar: @RawValue Drawable? = null,
    val filledStar: @RawValue Drawable? = null,
    val msgTextColor: Int = -1,
) : Parcelable
