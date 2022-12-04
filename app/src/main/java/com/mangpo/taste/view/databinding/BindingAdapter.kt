package com.mangpo.taste.view.databinding

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.android.material.imageview.ShapeableImageView
import com.mangpo.taste.util.convertDpToPx
import com.willy.ratingbar.BaseRatingBar

@BindingAdapter("marginStart")
fun setLayoutMarginStart(view: View, dimen: Int) {
    (view.layoutParams as ViewGroup.MarginLayoutParams).let {
        it.marginStart = convertDpToPx(view.context, dimen)
        view.layoutParams = it
    }
}

@BindingAdapter("marginEnd")
fun setLayoutMarginEnd(view: View, dimen: Int) {
    (view.layoutParams as ViewGroup.MarginLayoutParams).let {
        it.marginEnd = convertDpToPx(view.context, dimen)
        view.layoutParams = it
    }
}

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

@BindingAdapter("srbRating")
fun setSrbRatingSetting(view: BaseRatingBar, star: Int) {
    view.rating = star.toFloat()
}

@BindingAdapter(value = ["svg", "defaultDrawable"], requireAll = false)
fun setSvg(view: ImageView, svg: String?, defaultDrawable: Drawable?) {
    if (svg==null) {
        view.setImageDrawable(defaultDrawable)
    } else {
        GlideToVectorYou
            .init()
            .with(view.context)
            .load(Uri.parse(svg), view)
    }
}

@BindingAdapter("width")
fun setWidth(view: View, width: Int) {
    val params = view.layoutParams
    params.width = width
    view.layoutParams = params
}

@BindingAdapter("height")
fun setHeight(view: View, height: Int) {
    val params = view.layoutParams
    params.height = height
    view.layoutParams = params
}

@BindingAdapter(value = ["rt", "rb", "lt", "lb"], requireAll = false)
fun setLeftRadiusOfShapeableImageView(iv: ShapeableImageView, rt: Int=0, rb: Int=0, lt: Int=0, lb: Int=0) {
    val builder = iv.shapeAppearanceModel.toBuilder()
    builder.apply {
        setBottomLeftCornerSize(convertDpToPx(iv.context, lb).toFloat())
        setTopLeftCornerSize(convertDpToPx(iv.context, lt).toFloat())
        setBottomRightCornerSize(convertDpToPx(iv.context, rb).toFloat())
        setTopRightCornerSize(convertDpToPx(iv.context, rt).toFloat())
    }
    iv.shapeAppearanceModel = builder.build()
}

@BindingAdapter("widthPercentage")
fun setWidthPercentage(view: ShapeableImageView, widthPercentage: Float) {
    val params = (view.layoutParams as ConstraintLayout.LayoutParams)
    params.matchConstraintPercentWidth = widthPercentage
    view.layoutParams = params
}

@BindingAdapter("heightPercentage")
fun setHeightPercentage(view: View, heightPercentage: Float) {
    val params = (view.layoutParams as ConstraintLayout.LayoutParams)
    params.matchConstraintPercentHeight = heightPercentage
    view.layoutParams = params
}

@BindingAdapter("imgUri")
fun setImgUri(view: ImageView, uri: Uri?) {
    view.setImageURI(uri)
}