package com.mangpo.taste.view.calendar

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mangpo.taste.databinding.CalendarDayLayoutBinding

class DayViewContainer(view: View): ViewContainer(view) {
    private val binding: CalendarDayLayoutBinding = CalendarDayLayoutBinding.bind(view)

    val root: ConstraintLayout = binding.root
    val ovalView: View = binding.calendarDayOvalView
    val dayTv: TextView = binding.calendarDayTv
    val smallOvalView: View = binding.calendarDaySmallOvalView
}