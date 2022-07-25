package com.mangpo.taste.util

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.mangpo.taste.R
import kotlin.math.roundToInt

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

fun convertDpToPx(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).roundToInt()
}

fun getDeviceWidth(): Int = Resources.getSystem().displayMetrics.widthPixels

fun fadeIn(context: Context, view: View) {
    val fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    view.startAnimation(fadeInAnim)
    view.visibility = View.VISIBLE
}

fun fadeOut(context: Context, view: View) {
    val fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_out)
    view.startAnimation(fadeInAnim)
    view.visibility = View.INVISIBLE
}