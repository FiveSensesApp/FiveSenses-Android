package com.mangpo.taste.util

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

object DialogFragmentUtils {
    fun resizeWidth(context: Context, dialogFragment: DialogFragment, width: Float) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val window = dialogFragment.dialog?.window
        val params: ViewGroup.LayoutParams? = window?.attributes

        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)

            val deviceWidth = size.x
            params?.width = (deviceWidth * width).toInt()
            window?.attributes = params as WindowManager.LayoutParams
        } else {
            val rect = windowManager.currentWindowMetrics.bounds
            params?.width = (rect.width() * width).toInt()
            window?.attributes = params as WindowManager.LayoutParams
        }
    }
}