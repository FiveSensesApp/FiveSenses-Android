package com.mangpo.taste.view.model

import android.graphics.drawable.Drawable

data class RecordDetailResource(
    val dateTextColor: Int = -1,
    val character: Drawable? = null,
    val moreIcon: Drawable? = null,
    val shareIcon: Drawable? = null,
    val emptyStar: Drawable? = null,
    val filledStar: Drawable? = null,
)