package com.mangpo.taste.view.model

import android.graphics.drawable.Drawable

data class MonthlyCategory(
    val character: Drawable?  = null,
    val month: String,
    val textColor: Int = -1,
    val sense: String? = null,
    val cnt: Int = 0
)