package com.mangpo.taste.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentByCalendarBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.view.adpater.RecordShortAdapter
import com.mangpo.taste.view.calendar.CalendarHeaderViewContainer
import com.mangpo.taste.view.calendar.DayViewContainer
import com.mangpo.taste.view.model.Record
import com.mangpo.taste.viewmodel.FeedViewModel
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.IsoFields
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

@AndroidEntryPoint
class ByCalendarFragment : BaseFragment<FragmentByCalendarBinding, FeedViewModel>(FragmentByCalendarBinding::inflate) {
    private val mainVm: MainViewModel by activityViewModels()
    override val viewModel: FeedViewModel by viewModels()

    private var page: Int = 0
    private var isLast: Boolean = true
    private var deletedContentId: Int = -1

    private lateinit var dayViewContainer: DayViewContainer
    private lateinit var headerViewContainer: CalendarHeaderViewContainer
    private lateinit var recordShortAdapter: RecordShortAdapter
    private lateinit var recordDialogFragment: RecordDialogFragment

    var isCalendarOpen: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragment = this@ByCalendarFragment
        }

        initCalendarHeader()
        initDayViewContainer()
        initAdapter()
        initRecordDialog()
        observe()
    }

    override fun initAfterBinding() {
        //현재 주차의 일요일~토요일 기록 유무 데이터 가져오기
        val now: LocalDate = LocalDate.now()
        val week = now.get(WeekFields.ISO.weekOfYear())
        val sunday = now.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week.toLong()).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toString()
        val saturday = now.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week.toLong()).with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).toString()
        viewModel.getPresentPostsBetween(sunday, saturday)
    }

    private fun initCalendarHeader() {
        headerViewContainer = CalendarHeaderViewContainer(binding.byCalendarCv)
        headerViewContainer.setChangeDateListener(object : CalendarHeaderViewContainer.ChangeDateListener {
            override fun change(startDate: String, endDate: String) {
                viewModel.getPresentPostsBetween(startDate, endDate)
            }
        })
        binding.byCalendarCv.monthHeaderBinder = headerViewContainer
    }

    private fun initDayViewContainer() {
        binding.byCalendarCv.monthScrollListener = {
            if (isCalendarOpen) {
                viewModel.getPresentPostsBetween(LocalDate.of(it.year, it.month, 1).toString(), it.yearMonth.atEndOfMonth().toString())
            } else {
                viewModel.getPresentPostsBetween(it.weekDays[0][0].date.toString(), it.weekDays[0][6].date.toString())
            }
        }

        dayViewContainer = DayViewContainer(binding.byCalendarCv, null)
        dayViewContainer.setOnDayClickListener(object : DayViewContainer.OnDayClickListener {
            override fun onClick(oldDate: LocalDate, selectedDate: LocalDate) {
                binding.byCalendarCv.notifyDateChanged(selectedDate)   //현재 선택한 날짜 선택된 UI 로 변경
                oldDate?.let { binding.byCalendarCv.notifyDateChanged(oldDate) }   //이전에 선택한 날짜 선택하지 않은 UI 로 변경

                //월별 달력일 경우 주별 달력 상태로 UI 변경
                if (isCalendarOpen) {
                    changeCalendar()
                }

                clearPaging()   //리사이클러뷰 페이징 초기화
                recordShortAdapter.clearData()  //현재 리사이클러뷰에 보이고 있는 content 데이터 모두 삭제
                viewModel.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, null, selectedDate.toString()) //시각 기록 총 개수 조회
                getPosts(page, selectedDate.toString()) //선택된 날짜의 기록 데이터 불러오는 API 요청
            }
        })
        binding.byCalendarCv.dayBinder = dayViewContainer
        dayViewContainer.updateMonthConfiguration(1)    //주별 달력이 현재 날짜로 보이도록 설정
    }

    private fun initAdapter() {
        recordShortAdapter = RecordShortAdapter(mutableListOf(Record(2, null)))

        recordShortAdapter.setMyClickListener(object : RecordShortAdapter.MyClickListener {
            override fun onClick(content: ContentEntity) {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("content", content)
                recordDialogFragment.arguments = bundle
                recordDialogFragment.show(requireActivity().supportFragmentManager, null)
            }

            override fun changeFilter(filter: String) {
            }

            override fun changeFilter(filter: Int) {
            }

            override fun unmarkedDate() {
                dayViewContainer.unMarkDate(dayViewContainer.getSelectedDate())
            }
        })

        //무한스크롤 구현
        binding.byCalendarRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //스크롤이 최하단에 있을 때
                if (!binding.byCalendarRv.canScrollVertically(1)) {
                    if (!isLast) {  //마지막 페이지가 아니면 현재페이지+1, 현재 선택돼 있는 날짜의 getPosts API 호출
                        getPosts(page+1, dayViewContainer.getSelectedDate().toString())
                    }
                }
            }
        })

        binding.byCalendarRv.adapter = recordShortAdapter

        viewModel.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, null, LocalDate.now().toString()) //오늘 날짜에 대한 총 기록 개수 조회
        getPosts(page, LocalDate.now().toString())  //오늘 날짜에 대한 기록 조회
    }

    private fun initRecordDialog() {
        recordDialogFragment = RecordDialogFragment()
        recordDialogFragment.setEventListener(object : RecordDialogFragment.EventListener {
            override fun close(contentEntity: ContentEntity) {
                recordShortAdapter.updateData(contentEntity)
            }

            override fun delete(contentId: Int) {
                deletedContentId = contentId
                viewModel.deletePost(contentId)
            }
        })
    }

    //캘린더 레이아웃 높이 설정 함수
    private fun setCalendarClHeight(height: Int) {
        val params = binding.byCalendarCalendarCl.layoutParams
        params.height = height
        binding.byCalendarCalendarCl.layoutParams = params
    }

    private fun getPosts(page: Int, createDate: String) {
        viewModel.getPosts(SpfUtils.getIntEncryptedSpf("userId"), page, "id,desc", createDate, null, null)
    }

    private fun clearPaging() {
        page = 0
        isLast = true
    }

    private fun observe() {
        //기록 조회 API 호출 여부를 판단하기 위한 LiveData Observing
        mainVm.callGetPostsFlag.observe(viewLifecycleOwner, Observer {
            val callGetPostsFlag = it.getContentIfNotHandled()

            if (callGetPostsFlag!=null && callGetPostsFlag) {
                clearPaging()   //페이징 관련 데이터 초기화
                recordShortAdapter.clearData()  //현재 리사이클러뷰에 있는 content 데이터들 지우기

                //오늘 날짜와 달력에 선택된 날짜가 다를 경우 오늘 날짜가 보이는 주별 달력 UI 로 변경하기
                if (dayViewContainer.getSelectedDate()!= LocalDate.now()) {
                    dayViewContainer.setSelectedDate(LocalDate.now())   //캘린더의 선택된 날짜를 오늘 날짜로 변경
                    if (isCalendarOpen) {   //월별 달력 UI 였을 경우
                        changeCalendar()    //주별 달력 UI 로 변경
                    } else {    //주별 달력 UI 였을 경우
                        dayViewContainer.updateMonthConfiguration(1)  //선택된 날짜(=오늘 날짜)의 주별 달력 UI가 보이도록 변경
                    }
                }

                viewModel.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, null, dayViewContainer.getSelectedDate().toString()) //현재 선택된 날짜(=오늘 날짜)에 대한 총 기록 개수 조회
                getPosts(page, dayViewContainer.getSelectedDate().toString())   //현재 선택된 날짜(=오늘 날짜)의 기록 데이터 조회 API 요청
            }
        })

        viewModel.recordByDate.observe(viewLifecycleOwner, Observer {
            dayViewContainer.markRecordedDates(it)
        })

        viewModel.feedCnt.observe(viewLifecycleOwner, Observer {
            val feedCnt = it.getContentIfNotHandled()

            if (feedCnt!=null) {
                recordShortAdapter.setCnt(feedCnt)

                if (feedCnt==1) {
                    dayViewContainer.markDate(dayViewContainer.getSelectedDate())
                }
            }
        })

        viewModel.posts.observe(viewLifecycleOwner, Observer {
            val posts = it.getContentIfNotHandled()

            if (posts!=null) {
                page = posts.pageNumber
                isLast = posts.isLast
                recordShortAdapter.addData(posts.content)
            }
        })

        viewModel.deletePostResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                200 -> recordShortAdapter.removeData(deletedContentId)
                404 -> showToast("삭제 중 문제가 발생했습니다.")
                else -> {}
            }
        })
    }

    fun changeCalendar() {
        if (isCalendarOpen) {   //expand
            setCalendarClHeight(convertDpToPx(requireContext(), 187))    //달력 레이아웃 높이 187dp 로
            dayViewContainer.updateMonthConfiguration(1)  //선택된 날짜의 주별 달력 UI가 보이도록
        } else {    //fold
            setCalendarClHeight(convertDpToPx(requireContext(), 470))    //달력 레이아웃 높이 470dp 로
            dayViewContainer.updateMonthConfiguration(6)    //선택된 날짜의 월별 달력 UI가 보이도록
        }

        isCalendarOpen = !isCalendarOpen
        binding.invalidateAll()
    }
}