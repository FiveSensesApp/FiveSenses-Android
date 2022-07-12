package com.mangpo.taste.util

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

object BottomSheetDialogUtils {
    fun setupRatio(context: Context, bottomSheetDialog: BottomSheetDialog, ratio: Double) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)

            layoutParams.height = (size.y * ratio).toInt()

        } else {
            val rect = windowManager.currentWindowMetrics.bounds
            layoutParams.height = (rect.height() * ratio).toInt()
        }

        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}