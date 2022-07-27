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
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentNoTasteBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.view.calendar.CalendarHeaderViewContainer
import com.mangpo.taste.view.calendar.DayViewContainer
import com.mangpo.taste.viewmodel.MainViewModel
import java.time.LocalDate

class NoTasteFragment : BaseFragment<FragmentNoTasteBinding>(FragmentNoTasteBinding::inflate) {
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var sp: SharedPreferences
    private lateinit var dayViewContainer: DayViewContainer
    private lateinit var headerViewContainer: CalendarHeaderViewContainer

    override fun initAfterBinding() {
        sp = requireActivity().getSharedPreferences(getString(R.string.app_name), Application.MODE_PRIVATE)

        initDayViewContainer()
        initCalendarHeader()
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

    private fun initDayViewContainer() {
        dayViewContainer = DayViewContainer(binding.noTasteCv)
        dayViewContainer.setOnDayClickListener(object : DayViewContainer.OnDayClickListener {
            override fun onClick(oldDate: LocalDate, selectedDate: LocalDate) {
                binding.noTasteCv.notifyDateChanged(selectedDate)   //현재 선택한 날짜 선택된 UI 로 변경
                oldDate?.let { binding.noTasteCv.notifyDateChanged(oldDate) }   //이전에 선택한 날짜 선택하지 않은 UI 로 변경
            }
        })
        binding.noTasteCv.dayBinder = dayViewContainer
    }

    private fun initCalendarHeader() {
        headerViewContainer = CalendarHeaderViewContainer(binding.noTasteCv)
        binding.noTasteCv.monthHeaderBinder = headerViewContainer
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

        dayViewContainer.updateMonthConfiguration(1, null)    //접혀있을 땐 한줄만 보여주기
    }

    //펼쳐져있는 캘린더뷰 UI 화면 함수
    private fun setExpandCalendarUI() {
        setCalendarClHeight(convertDpToPx(requireContext(), 470))    //달력 레이아웃 높이 470dp 로
        binding.noTasteArrowIv.setImageResource(R.drawable.ic_fold_44)  //접기 아이콘으로 변경
        binding.noTasteArrowIv.tag = "fold" //이미지뷰 태그를 fold 로

        dayViewContainer.updateMonthConfiguration(6, headerViewContainer.getYearMonth())    //펼쳐져 있을 땐 6줄 보여주기
    }

    private fun observe() {
        //피드 타입 LiveDate Observer
        mainViewModel.feedType.observe(viewLifecycleOwner, Observer {
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

                    setFoldCalendarUI() //주별 달력이 보이도록 설정하기
                    dayViewContainer.setSelectedDate(LocalDate.now())   //현재 날짜로 변경
                    dayViewContainer.updateMonthConfiguration(1, null)

                    binding.noTasteCalendarCl.visibility = View.VISIBLE //캘린더 레이아웃 VISIBLE
                    binding.noTasteTimelineFilterRg.visibility = View.INVISIBLE
                    binding.noTasteSenseHsv.visibility = View.INVISIBLE
                    binding.noTasteScoreFilterRg.visibility = View.INVISIBLE
                }
            }
        })
    }
}