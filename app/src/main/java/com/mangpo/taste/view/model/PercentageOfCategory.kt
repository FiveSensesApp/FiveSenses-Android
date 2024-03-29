package com.mangpo.taste.view.model

import android.graphics.drawable.Drawable

data class PercentageOfCategory (
    val sense: String,
    val percentage: Float,
    val color: Int,
    val character: Drawable,
    val graph: Drawable,
    val cnt: Int
)