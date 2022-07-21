package com.mangpo.taste.view.calendar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mangpo.taste.databinding.CalendarHeaderBinding

class CalendarHeaderViewContainer(view: View): ViewContainer(view) {
    private val binding: CalendarHeaderBinding = CalendarHeaderBinding.bind(view)

    val monthTv: TextView = binding.calendarHeaderYearMonthTv
    val leftIv: ImageView = binding.calendarHeaderLeftArrowIv
    val rightIv: ImageView = binding.calendarHeaderRightArrowIv
}