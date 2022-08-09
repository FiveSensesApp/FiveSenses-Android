package com.mangpo.taste.view

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentFeedBinding
import com.mangpo.taste.util.setSpannableText
import com.mangpo.taste.viewmodel.MainViewModel

class FeedFragment : BaseFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate) {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val hasRecord: Boolean = true

    override fun initAfterBinding() {
        setStartDestination()   //기록이 있으면 timelineFragment, 없으면 noTasteFragment 로 startDestination 지정하기
        setMyEventListener()
        observe()

        setSpannableText(binding.feedMyTasteTv.text.toString(), requireContext(), R.color.GY_03, 6, binding.feedMyTasteTv.text.length, binding.feedMyTasteTv)   //나의 취향 뒷부분 텍스트 색상 변경
    }

    //기록이 있을 때/없을 때 각각 다른 StartDestination 을 가짐
    private fun setStartDestination() {
        if (!hasRecord)   //기록이 없을 때 -> NoTasteFragment 로 이동하기
            binding.feedFcv.findNavController().navigate(R.id.action_global_noTasteFragment)
    }

    private fun setMyEventListener() {
        //오른쪽 검색 아이콘 클릭 리스너
        binding.feedSearchRightIv.setOnClickListener {
            it.visibility = View.INVISIBLE  //INVISIBLE
            binding.feedSearchLeftIv.visibility = View.VISIBLE  //왼쪽 검색 아이콘 VISIBLE
            binding.feedSearchEt.visibility = View.VISIBLE  //검색 EditText VISIBLE
        }

        //나의 취향 터치뷰 클릭 리스너
        binding.feedTypeSelectTouchView.setOnClickListener {
            if ((requireActivity() as MainActivity).checkRecordFcvVisibility()==View.INVISIBLE) {   //MainActivity 의 recordFcv 가 INVISIBLE 일 때만 활성화
                if (binding.feedTypeSelectLayout.visibility == View.GONE) { //나의 취향 정렬 타입 선택하는 화면이 안보이고 있으면 보여주기
                    binding.feedBlurredView.visibility = View.VISIBLE   //투명 배경 VISIBLE
                    binding.feedTypeSelectLayout.visibility = View.VISIBLE
                    binding.feedTopLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_wh_bottom30)
                } else    //나의 취향 정렬 타입 선택하는 화면이 보이고 있으면 숨겨주기
                    hideTypeSelectLayout()
            }
        }

        //취향 정렬 타입 텍스트뷰 클릭 리스너
        binding.feedType1TouchCl.setOnClickListener(TouchViewListener(1))
        binding.feedType2TouchCl.setOnClickListener(TouchViewListener(2))
        binding.feedType3TouchCl.setOnClickListener(TouchViewListener(3))
    }

    //나의 취향 정렬 타입 선택하는 화면 숨겨주기
    private fun hideTypeSelectLayout() {
        binding.feedBlurredView.visibility = View.INVISIBLE //투명 배경 INVISIBLE
        binding.feedTypeSelectLayout.visibility = View.GONE
        binding.feedTopLayout.background = null
    }

    private fun observe() {
        //피드 필터 타입 Observe
        mainViewModel.feedType.observe(viewLifecycleOwner, Observer {
            if (hasRecord) { //기록이 있을 때만
                when (it) {
                    getString(R.string.title_timeline) -> binding.feedFcv.findNavController().navigate(R.id.action_global_timelineFragment)
                    getString(R.string.title_by_sense) -> binding.feedFcv.findNavController().navigate(R.id.action_global_bySenseFragment)
                    getString(R.string.title_by_score) -> binding.feedFcv.findNavController().navigate(R.id.action_global_byScoreFragment)
                    getString(R.string.title_by_calendar) -> binding.feedFcv.findNavController().navigate(R.id.action_global_byCalendarFragment)
                }
            }
        })
    }

    //터치뷰 클릭리스너 이너 클래스
    inner class TouchViewListener(private val order: Int): View.OnClickListener {
        private val typeTextList = listOf(getString(R.string.title_timeline), getString(R.string.title_by_sense), getString(R.string.title_by_score), getString(R.string.title_by_calendar))
        private val typeIconList = listOf<Drawable>(ContextCompat.getDrawable(requireContext(), R.drawable.ic_timeline_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_sense_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar_24)!!)

        private lateinit var clickedText: String
        private lateinit var clickedIcon: Drawable

        override fun onClick(v: View?) {
            when (order) {
                1 -> {
                    clickedText = binding.feedType1Tv.text.toString()
                    clickedIcon = binding.feedType1Iv.drawable
                }
                2 -> {
                    clickedText = binding.feedType2Tv.text.toString()
                    clickedIcon = binding.feedType2Iv.drawable
                }
                else -> {
                    clickedText = binding.feedType3Tv.text.toString()
                    clickedIcon = binding.feedType3Iv.drawable
                }
            }


            mainViewModel.setFeedType(clickedText)  //피드 타입 LiveData 변경
            binding.feedMyTasteTv.text = "나의 취향 $clickedText" //피드 타입 텍스트 변경
            setSpannableText(binding.feedMyTasteTv.text.toString(), requireContext(), R.color.GY_03, 6, binding.feedMyTasteTv.text.length, binding.feedMyTasteTv)   //나의 취향 뒷부분 텍스트 색상 변경

            hideTypeSelectLayout()  //나의 취향 정렬 타입 선택하는 화면 숨겨주기

            //선택한 타입 텍스트&아이콘 제외하고 나머지를 typeSelectLayout 에 보여주기
            val removedTypeTextList: List<String> = typeTextList.filterNot { it == clickedText }
            val removedTypeIconList: List<Drawable> = typeIconList.filterNot { it == clickedIcon }

            binding.feedType1Tv.text = removedTypeTextList[0]
            binding.feedType1Iv.setImageDrawable(removedTypeIconList[0])
            binding.feedType2Tv.text = removedTypeTextList[1]
            binding.feedType2Iv.setImageDrawable(removedTypeIconList[1])
            binding.feedType3Tv.text = removedTypeTextList[2]
            binding.feedType3Iv.setImageDrawable(removedTypeIconList[2])
        }
    }
}