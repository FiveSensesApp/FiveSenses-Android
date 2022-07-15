package com.mangpo.taste.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlin.math.roundToInt

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

fun convertDpToPx(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).roundToInt()
}