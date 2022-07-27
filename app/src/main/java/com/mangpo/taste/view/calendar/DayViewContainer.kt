package com.mangpo.taste.view.calendar

import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mangpo.taste.R
import com.mangpo.taste.databinding.CalendarDayLayoutBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.util.getDeviceWidth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class DayViewContainer(calendarView: CalendarView): DayBinder<DayViewContainer.DayViewContainer> {
    interface OnDayClickListener {
        fun onClick(oldDate: LocalDate, selectedDate: LocalDate)
    }

    private val daysOfWeek = arrayOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )

    private var calendarView: CalendarView = calendarView
    private var selectedDate: LocalDate = LocalDate.now()

    private lateinit var onDayClickListener: OnDayClickListener

    override fun bind(container: DayViewContainer, day: CalendarDay) {
        container.bind(day)
    }

    override fun create(view: View): DayViewContainer = DayViewContainer(view)

    fun setOnDayClickListener(onDayClickListener: OnDayClickListener) {
        this.onDayClickListener = onDayClickListener
    }

    fun setSelectedDate(selectedDate: LocalDate) {
        val oldDate = this.selectedDate  //이전에 선택했던 날짜
        this.selectedDate = selectedDate //현재 선택한 날짜
        onDayClickListener.onClick(oldDate, selectedDate)
    }


    fun updateMonthConfiguration(week: Int, yearMonth: YearMonth?) {
        if (week==1) {  //접혀있을 땐 한줄만 보여주기
            calendarView.updateMonthConfiguration(
                inDateStyle = InDateStyle.ALL_MONTHS,
                maxRowCount = week,
                hasBoundaries = false
            )

            initWeekCalendarView() //선택된 날짜가 포함된 주의 달력을 보여주기
        } else {    //펼쳐져 있을 땐 6줄 보여주기
            calendarView.updateMonthConfiguration(
                inDateStyle = InDateStyle.ALL_MONTHS,
                maxRowCount = week,
                hasBoundaries = true
            )

            initMonthCalendarView(yearMonth!!) //주별 달력에서 현재 보여주고 있는 월의 달력을 보여주기
        }
    }

    private fun initWeekCalendarView() {
        val yearMonth = YearMonth.of(selectedDate.year, selectedDate.monthValue)
        val firstMonth = yearMonth.minusMonths(10)
        val lastMonth = yearMonth.plusMonths(10)

        calendarView.setup(firstMonth, lastMonth, daysOfWeek.first())
        calendarView.scrollToDate(selectedDate)
    }

    private fun initMonthCalendarView(yearMonth: YearMonth) {
        val firstMonth = yearMonth.minusMonths(10)
        val lastMonth = yearMonth.plusMonths(10)

        calendarView.setup(firstMonth, lastMonth, daysOfWeek.first())
        calendarView.scrollToMonth(yearMonth)
    }

    inner class DayViewContainer(view: View): ViewContainer(view) {
        private val binding: CalendarDayLayoutBinding = CalendarDayLayoutBinding.bind(view)

        fun bind(day: CalendarDay) {
            binding.calendarDayTv.text = day.date.dayOfMonth.toString()   //날짜 보여주기

            //일요일 -> marginStart: 14dp, 토요일 -> marginEnd: 14dp && 각 넓이를 (전체 넓이 - 28dp)/7 로 설정
            val params = binding.root.layoutParams as LinearLayout.LayoutParams
            if (day.date.dayOfWeek== DayOfWeek.SUNDAY)
                params.marginStart = convertDpToPx(view.context, 14)
            else if (day.date.dayOfWeek== DayOfWeek.SATURDAY)
                params.marginEnd = convertDpToPx(view.context, 14)
            params.width = (getDeviceWidth() - convertDpToPx(view.context, 28))/7
            binding.root.layoutParams = params

            if (day.owner == DayOwner.THIS_MONTH) { //이번달
                binding.root.visibility = View.VISIBLE    //날짜뷰 VISIBLE

                if (selectedDate==day.date) {   //선택된 날짜
                    binding.calendarDayOvalView.visibility = View.VISIBLE    //전체 동그라미 뷰 VISIBLE
                    binding.calendarDayTv.setTextColor(ContextCompat.getColor(view.context, R.color.WH))  //텍스트 색상 하얀색으로

                    if (day.date == LocalDate.now())    //오늘 날짜면
                        binding.calendarDayOvalView.background = ContextCompat.getDrawable(view.context, R.drawable.bg_rd2_oval) //전체 동그라미 뷰 RD2
                    else    //오늘 날짜가 아니면
                        binding.calendarDayOvalView.background = ContextCompat.getDrawable(view.context, R.drawable.bg_gy04_oval) //전체 동그라미 뷰 GY04
                } else {    //선택되지 않은 날짜
                    binding.calendarDayOvalView.visibility = View.INVISIBLE  //전체 동그라미 뷰 INVISIBLE

                    if (day.date== LocalDate.now()) //오늘 날짜면
                        binding.calendarDayTv.setTextColor(ContextCompat.getColor(view.context, R.color.RD_2))    //텍스트 색상 빨간색으로
                    else    //오늘 날짜가 아니면
                        binding.calendarDayTv.setTextColor(ContextCompat.getColor(view.context, R.color.BK))  //텍스트 색상 검정색으로
                }

                //날짜 선택했을 때
                binding.root.setOnClickListener {
                    val oldDate = selectedDate  //이전에 선택했던 날짜
                    selectedDate = day.date //현재 선택한 날짜
                    onDayClickListener.onClick(oldDate, selectedDate)
                }
            } else  //이전달 or 다음달
                binding.root.visibility = View.INVISIBLE  //날짜뷰 INVISIBLE
        }
    }
}