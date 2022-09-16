package com.mangpo.taste.view.databinding

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.mangpo.taste.util.convertDpToPx
import com.willy.ratingbar.BaseRatingBar

@BindingAdapter("marginTop")
fun setLayoutMarginTop(view: View, dimen: Int) {
    (view.layoutParams as ViewGroup.MarginLayoutParams).let {
        it.topMargin = convertDpToPx(view.context, dimen)
        view.layoutParams = it
    }
}

@BindingAdapter("marginBottom")
fun setLayoutMarginBottom(view: View, dimen: Int) {
    (view.layoutParams as ViewGroup.MarginLayoutParams).let {
        it.bottomMargin = convertDpToPx(view.context, dimen)
        view.layoutParams = it
    }
}

@BindingAdapter("srbEmptyDrawable")
fun setSrbEmptySetting(view: BaseRatingBar, emptyDrawable: Drawable) {
    view.setEmptyDrawable(emptyDrawable)
}

@BindingAdapter("srbFilledDrawable")
fun setSrbFilledSetting(view: BaseRatingBar, filledDrawable: Drawable) {
    view.setFilledDrawable(filledDrawable)
}