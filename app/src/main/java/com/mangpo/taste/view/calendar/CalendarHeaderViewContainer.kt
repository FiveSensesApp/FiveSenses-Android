package com.mangpo.taste.view.calendar

import android.view.View
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mangpo.taste.databinding.CalendarHeaderBinding
import java.time.LocalDate
import java.time.YearMonth

class CalendarHeaderViewContainer(calendarView: CalendarView): MonthHeaderFooterBinder<CalendarHeaderViewContainer.CalendarHeaderViewContainer> {
    interface ChangeDateListener {
        fun change(startDate: String, endDate: String)
    }

    private var calendarView = calendarView
    private var yearMonth: YearMonth = YearMonth.now()

    private lateinit var changeDateListener: ChangeDateListener

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
                if (calendarView.maxRowCount==1) {    //주별 달력
                    calendarView.scrollToDate(month.weekDays[0][0].date.minusDays(7L))  //7일 전으로 이동
                    changeDateListener.change(month.weekDays[0][0].date.minusDays(7L).toString(), month.weekDays[0][6].date.minusDays(7L).toString())
                } else {    //월별 달력
                    val minusMonth = month.yearMonth.minusMonths(1L)
                    calendarView.scrollToMonth(minusMonth)  //한달 전으로 이동
                    changeDateListener.change(LocalDate.of(minusMonth.year, minusMonth.monthValue, 1).toString(), minusMonth.atEndOfMonth().toString())
                }
            }

            //오른쪽 화살표 클릭 리스너
            binding.calendarHeaderRightArrowIv.setOnClickListener {
                if (calendarView.maxRowCount==1) {    //주별 달력
                    calendarView.scrollToDate(month.weekDays[0][0].date.plusDays(7L))   //7일 후로 이동
                    changeDateListener.change(month.weekDays[0][0].date.plusDays(7L).toString(), month.weekDays[0][6].date.plusDays(7L).toString())
                } else {    //월별 달력
                    val addedMonth = month.yearMonth.plusMonths(1L)
                    calendarView.scrollToMonth(addedMonth)  //한달 후로 이동
                    changeDateListener.change(LocalDate.of(addedMonth.year, addedMonth.monthValue, 1).toString(), addedMonth.atEndOfMonth().toString())
                }
            }
        }
    }

    override fun bind(container: CalendarHeaderViewContainer, month: CalendarMonth) {
        container.bind(month)
    }

    override fun create(view: View): CalendarHeaderViewContainer = CalendarHeaderViewContainer(view)

    fun setChangeDateListener(changeDateListener: ChangeDateListener) {
        this.changeDateListener = changeDateListener
    }
}