package com.mangpo.taste.util

import android.content.Context
import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mangpo.taste.R
import com.willy.ratingbar.BaseRatingBar
import kotlin.math.roundToInt

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

fun convertDpToPx(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).roundToInt()
}

fun getDeviceWidth(): Int = Resources.getSystem().displayMetrics.widthPixels

fun getDeviceHeight(): Int = Resources.getSystem().displayMetrics.heightPixels

fun fadeIn(context: Context, view: View) {
    val fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    view.startAnimation(fadeInAnim)
    view.visibility = View.VISIBLE
}

fun fadeOut(context: Context, view: View) {
    val fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_out)
    view.startAnimation(fadeInAnim)
    view.visibility = View.GONE
}

//텍스트가 두개의 텍스트색상을 가질 경우 사용
fun setSpannableText(text: String, context: Context, color: Int, start: Int, end: Int, tv: TextView) {
    val ssb: SpannableStringBuilder = SpannableStringBuilder(text)
    ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    tv.text = ssb
}

//RatingBar 세팅
fun BaseRatingBar.setting(emptyDrawable: Int, filledDrawable: Int, star: Float) {
    this.setEmptyDrawableRes(emptyDrawable)
    this.setFilledDrawableRes(filledDrawable)
    this.rating = star
}