package com.mangpo.taste.view.databinding

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.mangpo.taste.util.convertDpToPx

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