package com.mangpo.taste.view.calendar

import android.view.View
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mangpo.taste.databinding.CalendarHeaderBinding
import java.time.YearMonth

class CalendarHeaderViewContainer(calendarView: CalendarView): MonthHeaderFooterBinder<CalendarHeaderViewContainer.CalendarHeaderViewContainer> {
    private var calendarView = calendarView
    private var yearMonth: YearMonth = YearMonth.now()

    inner class CalendarHeaderViewContainer(view: View): ViewContainer(view) {
        private val binding: CalendarHeaderBinding = CalendarHeaderBinding.bind(view)

        fun bind(month: CalendarMonth) {
            if (calendarView.maxRowCount==1) {    //주별 달력
                binding.calendarHeaderYearMonthTv.text = "${month.weekDays[0][0].date.year}년 ${month.weekDays[0][0].date.monthValue}월"
                yearMonth = YearMonth.of(month.weekDays[0][0].date.year, month.weekDays[0][0].date.monthValue)
            } else {    //월별 달력
                binding.calendarHeaderYearMonthTv.text = "${month.year}년 ${month.month}월"
                yearMonth = YearMonth.of(month.year, month.month)
            }

            //왼쪽 화살표 클릭 리스너
            binding.calendarHeaderLeftArrowIv.setOnClickListener {
                if (calendarView.maxRowCount==1)    //주별 달력
                    calendarView.scrollToDate(month.weekDays[0][0].date.minusDays(7L))  //7일 전으로 이동
                else    //월별 달력
                    calendarView.scrollToMonth(month.yearMonth.minusMonths(1L)) //한달 전으로 이동
            }

            //오른쪽 화살표 클릭 리스너
            binding.calendarHeaderRightArrowIv.setOnClickListener {
                if (calendarView.maxRowCount==1)    //주별 달력
                    calendarView.scrollToDate(month.weekDays[0][0].date.plusDays(7L))   //7일 후로 이동
                else    //월별 달력
                    calendarView.scrollToMonth(month.yearMonth.plusMonths(1L))  //한달 후로 이동
            }
        }
    }

    override fun bind(container: CalendarHeaderViewContainer, month: CalendarMonth) {
        container.bind(month)
    }

    override fun create(view: View): CalendarHeaderViewContainer = CalendarHeaderViewContainer(view)

    fun getYearMonth(): YearMonth = yearMonth
}