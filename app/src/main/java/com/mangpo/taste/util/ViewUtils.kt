package com.mangpo.taste.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.willy.ratingbar.BaseRatingBar
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

fun convertDpToPx(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).roundToInt()
}

fun convertPxToDp(context: Context, px: Int): Int {
    val density = context.resources.displayMetrics.density
    return (px / density).roundToInt()
}

fun getDeviceWidth(): Int = Resources.getSystem().displayMetrics.widthPixels

fun getDeviceHeight(): Int = Resources.getSystem().displayMetrics.heightPixels

fun View.translateX(duration: Long, value: Float) {
    ObjectAnimator.ofFloat(this, "translationX", value).apply {
        this.duration = duration
        start()
    }
}

fun View.fadeIn(duration: Long) {
    ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).apply {
        this.duration = duration
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                this@fadeIn.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(p0: Animator) {
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })
        start()
    }
}

fun View.fadeOut(duration: Long) {
    ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
        this.duration = duration
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                this@fadeOut.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })
        start()
    }
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

//이전 프래그먼트에게 값 넘겨주기
fun <T> Fragment.setNavigationResult(key: String, result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun Activity.setKeyboardVisibilityEvent(iv: ImageView, btn: AppCompatButton) {
    KeyboardVisibilityEvent.setEventListener(this) {
        val stepIvParams = iv.layoutParams as ConstraintLayout.LayoutParams
        val nextBtnParams = btn.layoutParams as ConstraintLayout.LayoutParams
        if (it) {   //키보드 올라와 있으면
            stepIvParams.topMargin = convertDpToPx(applicationContext, 36) //marginTop 36dp
            nextBtnParams.bottomMargin =
                convertDpToPx(applicationContext, 15) //marginBottom 15dp
        } else {    //키보드 내려와 있으면
            stepIvParams.topMargin = convertDpToPx(applicationContext, 74) //marginTop 74dp
            nextBtnParams.bottomMargin =
                convertDpToPx(applicationContext, 122) //marginBottom 122dp
        }

        iv.layoutParams = stepIvParams
        btn.layoutParams = nextBtnParams
    }
}

fun matchRegex(text: String, regex: Regex): Boolean = text.matches(regex)

fun readTxtFile(context: Context, raw: Int): String? {
    return try {
        val inputStream: InputStream = context.resources.openRawResource(raw)
        val b = ByteArray(inputStream.available())
        inputStream.read(b)

        String(b)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun formatDate(date: String, format: String): String {
    return LocalDate.parse(date).format(DateTimeFormatter.ofPattern(format))
}

fun goUrlPage(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}