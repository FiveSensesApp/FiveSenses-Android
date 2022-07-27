package com.mangpo.taste.view

import androidx.navigation.fragment.findNavController
import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentByCalendarBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.view.adpater.RecordShortAdapter
import com.mangpo.taste.view.calendar.CalendarHeaderViewContainer
import com.mangpo.taste.view.calendar.DayViewContainer
import com.mangpo.taste.view.model.Record
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ByCalendarFragment : BaseFragment<FragmentByCalendarBinding>(FragmentByCalendarBinding::inflate) {
    private val recordEntities: ArrayList<RecordEntity> = arrayListOf<RecordEntity>(
        RecordEntity(
            0,
            "시각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.07.06",
            4.0f
        ),
        RecordEntity(
            2,
            "후각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.07.13",
            4.0f
        ),
        RecordEntity(
            3,
            "미각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.07.20",
            4.0f
        ),
        RecordEntity(
            4,
            "촉각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.07.27",
            4.0f
        ),
        RecordEntity(
            5,
            "모르겠어요 감각 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.07.27",
            4.0f
        ),
        RecordEntity(0, "시각으로 감각한 좋아하는 거", null, "2022.06.01", 4.0f),
        RecordEntity(1, "청각으로 감각한 좋아하는 거", null, "2022.06.08", 4.0f),
        RecordEntity(2, "후각으로 감각한 좋아하는 거", null, "2022.06.15", 4.0f),
        RecordEntity(3, "미각으로 감각한 좋아하는 거", null, "2022.06.22", 4.0f),
        RecordEntity(4, "촉각으로 감각한 좋아하는 거", null, "2022.06.29", 4.0f),
        RecordEntity(5, "모르겠어요 감각 좋아하는 거", null, "2022.07.20", 4.0f)
    )

    private lateinit var dayViewContainer: DayViewContainer
    private lateinit var headerViewContainer: CalendarHeaderViewContainer
    private lateinit var recordShortAdapter: RecordShortAdapter

    override fun initAfterBinding() {
        setMyEventListener()

        initDayViewContainer()
        initCalendarHeader()

        initAdapter()
    }

    private fun setMyEventListener() {
        //달력 접기/펼치기 터치뷰 클릭 리스너
        binding.byCalendarArrowTouchView.setOnClickListener {
            if (binding.byCalendarArrowIv.tag=="fold") {  //펼쳐져 있을 때 -> 접기
                setFoldCalendarView()
            } else {    //접혀 있을 때 -> 펼치기
                setCalendarClHeight(convertDpToPx(requireContext(), 470))    //달력 레이아웃 높이 470dp 로
                binding.byCalendarArrowIv.setImageResource(R.drawable.ic_fold_44)  //접기 아이콘으로 변경
                binding.byCalendarArrowIv.tag = "fold" //이미지뷰 태그를 fold 로

                dayViewContainer.updateMonthConfiguration(6, headerViewContainer.getYearMonth())
            }
        }
    }

    private fun initDayViewContainer() {
        dayViewContainer = DayViewContainer(binding.byCalendarCv, recordEntities.map { it.date }.distinct())
        dayViewContainer.setOnDayClickListener(object : DayViewContainer.OnDayClickListener {
            override fun onClick(oldDate: LocalDate, selectedDate: LocalDate) {
                binding.byCalendarCv.notifyDateChanged(selectedDate)   //현재 선택한 날짜 선택된 UI 로 변경
                oldDate?.let { binding.byCalendarCv.notifyDateChanged(oldDate) }   //이전에 선택한 날짜 선택하지 않은 UI 로 변경

                setFoldCalendarView()   //주별 달력으로 변경
                setRecordData(selectedDate) //선택된 날짜의 기록 데이터로 RecyclerView 에 데이터 전달하기
            }
        })
        binding.byCalendarCv.dayBinder = dayViewContainer
        dayViewContainer.updateMonthConfiguration(1, null)    //주별 달력이 현재 날짜로 보이도록 설정
    }

    private fun initCalendarHeader() {
        headerViewContainer = CalendarHeaderViewContainer(binding.byCalendarCv)
        binding.byCalendarCv.monthHeaderBinder = headerViewContainer
    }

    //캘린더 레이아웃 높이 설정 함수
    private fun setCalendarClHeight(height: Int) {
        val params = binding.byCalendarCalendarCl.layoutParams
        params.height = height
        binding.byCalendarCalendarCl.layoutParams = params
    }

    private fun initAdapter() {
        recordShortAdapter = RecordShortAdapter()
        recordShortAdapter.setMyClickListener(object : RecordShortAdapter.MyClickListener {
            override fun onClick(record: Record) {
                val action = BySenseFragmentDirections.actionGlobalRecordDialogFragment(record)
                findNavController().navigate(action)
            }
        })
        setRecordData(LocalDate.now())  //오늘 날짜의 기록 데이터로 RecyclerView 에 데이터 전달하기
        binding.byCalendarRv.adapter = recordShortAdapter
    }

    private fun setRecordData(date: LocalDate) {
        val todayRecord = recordEntities.filter { LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy.MM.dd"))==date }
        val records: ArrayList<Record> = arrayListOf(Record(2, null))
        for (record in todayRecord) {
            records.add(Record(3, record))
        }

        recordShortAdapter.setData(records)
    }

    private fun setFoldCalendarView() {
        setCalendarClHeight(convertDpToPx(requireContext(), 187))   //달력 레이아웃 높이 187dp 로
        binding.byCalendarArrowIv.setImageResource(R.drawable.ic_expand_46)    //펼치기 아이콘으로 변경
        binding.byCalendarArrowIv.tag = "expand"   //이미지뷰 태그를 expand 로

        dayViewContainer.updateMonthConfiguration(1, null)
    }
}