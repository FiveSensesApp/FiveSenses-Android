package com.mangpo.taste.view

import android.app.Application
import android.content.SharedPreferences
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentNoTasteBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.view.calendar.DayViewContainer
import com.mangpo.taste.viewmodel.MainViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class NoTasteFragment : BaseFragment<FragmentNoTasteBinding>(FragmentNoTasteBinding::inflate) {
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var sp: SharedPreferences

    override fun initAfterBinding() {
        sp = requireActivity().getSharedPreferences(getString(R.string.app_name), Application.MODE_PRIVATE)

        setMyEventListener()
        observe()

        //tryTv 텍스트 특정 부분 색상 GY_04로 변경
        val nickname = "워니버니"
        val ssb: SpannableStringBuilder = SpannableStringBuilder("${nickname}님,\n처음으로 취향을 감각해보세요!")
        ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.GY_04)), 0, nickname.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.noTasteTryTv.text = ssb

        //가이드 버튼 visibility 여부 확인
        if (sp.getBoolean("isGuideClicked", false))
            binding.noTasteHowToUseBtn.visibility = View.INVISIBLE
        else
            binding.noTasteHowToUseBtn.visibility = View.VISIBLE
    }

    private fun initCalendarView(yearMonth: YearMonth) {
        val firstMonth = yearMonth.minusMonths(10)
        val lastMonth = yearMonth.plusMonths(10)
        val daysOfWeek = arrayOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )
        binding.noTasteCv.setup(firstMonth, lastMonth, daysOfWeek.first())
        binding.noTasteCv.scrollToMonth(yearMonth)
//        binding.noTasteCv.scrollToDate(LocalDate.now())
    }

    private fun setCalendarViewClass() {
        binding.noTasteCv.dayBinder = object : DayBinder<DayViewContainer> {
            var selectedDate: LocalDate = LocalDate.now()

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.dayTv.text = day.date.dayOfMonth.toString()   //날짜 보여주기

                if (day.owner == DayOwner.THIS_MONTH) { //이번달
                    binding.noTasteYearMonthTv.text = "${day.date.year}년 ${day.date.month}월"

                    container.root.visibility = View.VISIBLE

                    if (selectedDate==day.date) {   //선택된 날짜
                        container.ovalView.visibility = View.VISIBLE    //전체 동그라미 뷰 VISIBLE

                        if (day.date == LocalDate.now())    //오늘 날짜면
                            container.dayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.RD_2))    //텍스트 색상 빨간색으로
                        else    //오늘 날짜가 아니면
                            container.dayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.WH))  //텍스트 색상 하얀색으로
                    } else {    //선택되지 않은 날짜
                        container.ovalView.visibility = View.INVISIBLE  //전체 동그라미 뷰 INVISIBLE

                        if (day.date== LocalDate.now()) //오늘 날짜면
                            container.dayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.RD_2))    //텍스트 색상 빨간색으로
                        else    //오늘 날짜가 아니면
                            container.dayTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.BK))  //텍스트 색상 검정색으로
                    }

                    //날짜 선택했을 때
                    container.root.setOnClickListener {
                        val oldDate = selectedDate  //이전에 선택했던 날짜
                        selectedDate = day.date //현재 선택한 날짜
                        binding.noTasteCv.notifyDateChanged(day.date)   //현재 선택한 날짜 선택된 UI 로 변경
                        oldDate?.let { binding.noTasteCv.notifyDateChanged(oldDate) }   //이전에 선택한 날짜 선택하지 않은 UI 로 변경
                    }
                } else  //이전달 or 다음달
                    container.root.visibility = View.INVISIBLE
            }

            override fun create(view: View): DayViewContainer = DayViewContainer(view)
        }
    }

    private fun setMyEventListener() {
        //가이드 버튼 클릭 리스너
        binding.noTasteHowToUseBtn.setOnClickListener {
            //가이드 노션 페이지로 이동(나중에)
            it.visibility = View.INVISIBLE  //visibility 를 INVISIBLE

            //클릭 여부를 SharedPreferences 에 저장하기
            val editor  : SharedPreferences.Editor = sp.edit()
            editor.putBoolean("isGuideClicked", true)
            editor.commit()
        }

        //달력 접기/펼치기 터치뷰 클릭 리스너
        binding.noTasteArrowTouchView.setOnClickListener {
            if (binding.noTasteArrowIv.tag=="fold") {  //펼쳐져 있을 때 -> 접기
                setFoldCalendarUI()
                setTryClMargin(convertDpToPx(requireContext(), 136))    //취향을 감각해보세요! 레이아웃 topMarin 136dp
            } else {    //접혀 있을 때 -> 펼치기
                setExpandCalendarUI()
                setTryClMargin(convertDpToPx(requireContext(), 419))    //취향을 감각해보세요! 레이아웃 topMarin 419dp
            }

            initCalendarView(YearMonth.now())
        }
    }

    //캘린더 레이아웃 높이 설정 함수
    private fun setCalendarClHeight(height: Int) {
        val params = binding.noTasteCalendarCl.layoutParams
        params.height = height
        binding.noTasteCalendarCl.layoutParams = params
    }

    //취향을 감각해보세요! 레이아웃 마진 설정 함수
    private fun setTryClMargin(margin: Int) {
        val params = binding.noTasteTryCl.layoutParams as ConstraintLayout.LayoutParams
        params.setMargins(convertDpToPx(requireContext(), 20), margin, convertDpToPx(requireContext(), 20), 0)
        binding.noTasteTryCl.layoutParams = params
    }

    //접혀있는 캘린더뷰 UI 화면 함수
    private fun setFoldCalendarUI() {
        setCalendarClHeight(convertDpToPx(requireContext(), 187))   //달력 레이아웃 높이 187dp 로
        binding.noTasteArrowIv.setImageResource(R.drawable.ic_expand_46)    //펼치기 아이콘으로 변경
        binding.noTasteArrowIv.tag = "expand"   //이미지뷰 태그를 expand 로

        //접혀있을 땐 한줄만 보여주기
        binding.noTasteCv.updateMonthConfiguration(
            inDateStyle = InDateStyle.ALL_MONTHS,
            maxRowCount = 1,
            hasBoundaries = false
        )
    }

    //펼쳐져있는 캘린더뷰 UI 화면 함수
    private fun setExpandCalendarUI() {
        setCalendarClHeight(convertDpToPx(requireContext(), 470))    //달력 레이아웃 높이 470dp 로
        binding.noTasteArrowIv.setImageResource(R.drawable.ic_fold_44)  //접기 아이콘으로 변경
        binding.noTasteArrowIv.tag = "fold" //이미지뷰 태그를 fold 로

        //펼쳐져 있을 땐 6줄 보여주기
        binding.noTasteCv.updateMonthConfiguration(
            inDateStyle = InDateStyle.ALL_MONTHS,
            maxRowCount = 6,
            hasBoundaries = true
        )
    }

    private fun observe() {
        //피드 타입 LiveDate Observer
        mainViewModel.feedType.observe(viewLifecycleOwner, Observer {
            setFoldCalendarUI()

            when (it) {
                getString(R.string.title_timeline) -> {
                    setTryClMargin(convertDpToPx(requireContext(), 9))  //취향을 감각해보세요! 레이아웃 topMarin 9dp

                    binding.noTasteCalendarCl.visibility = View.GONE    //캘린더 레이아웃 GONE
                    binding.noTasteTimelineFilterRg.visibility = View.VISIBLE
                    binding.noTasteSenseHsv.visibility = View.INVISIBLE
                    binding.noTasteScoreFilterRg.visibility = View.INVISIBLE
                }
                getString(R.string.title_by_sense) -> {
                    setTryClMargin(convertDpToPx(requireContext(), 9))  //취향을 감각해보세요! 레이아웃 topMarin 9dp

                    binding.noTasteCalendarCl.visibility = View.GONE    //캘린더 레이아웃 GONE
                    binding.noTasteTimelineFilterRg.visibility = View.INVISIBLE
                    binding.noTasteSenseHsv.visibility = View.VISIBLE
                    binding.noTasteScoreFilterRg.visibility = View.INVISIBLE
                }
                getString(R.string.title_by_score) -> {
                    setTryClMargin(convertDpToPx(requireContext(), 9))  //취향을 감각해보세요! 레이아웃 topMarin 9dp

                    binding.noTasteCalendarCl.visibility = View.GONE    //캘린더 레이아웃 GONE
                    binding.noTasteTimelineFilterRg.visibility = View.INVISIBLE
                    binding.noTasteSenseHsv.visibility = View.INVISIBLE
                    binding.noTasteScoreFilterRg.visibility = View.VISIBLE
                }
                getString(R.string.title_by_calendar) -> {
                    setTryClMargin(convertDpToPx(requireContext(), 136))    //취향을 감각해보세요! 레이아웃 topMarin 136dp
                    initCalendarView(YearMonth.now())
                    setCalendarViewClass()

                    binding.noTasteCalendarCl.visibility = View.VISIBLE //캘린더 레이아웃 VISIBLE
                    binding.noTasteTimelineFilterRg.visibility = View.INVISIBLE
                    binding.noTasteSenseHsv.visibility = View.INVISIBLE
                    binding.noTasteScoreFilterRg.visibility = View.INVISIBLE
                }
            }
        })
    }
}