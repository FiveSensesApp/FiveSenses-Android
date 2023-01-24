package com.mangpo.taste.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentFeedBinding
import com.mangpo.taste.util.*
import com.mangpo.taste.viewmodel.FeedViewModel
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel>(FragmentFeedBinding::inflate) {
    override val viewModel: FeedViewModel by viewModels()
    private val mainVm: MainViewModel by activityViewModels()

    private lateinit var typeIconList: List<Drawable>
    private lateinit var typeTextList: List<String>

    var isTypeSelectShown: Boolean = false
    var selectedType = "나의 취향 | 타임라인"
    lateinit var removedTypeTextList: List<String>
    lateinit var removedTypeIconList: List<Drawable>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragment = this@FeedFragment
            lifecycleOwner = viewLifecycleOwner
        }

        typeIconList = listOf<Drawable>(ContextCompat.getDrawable(requireContext(), R.drawable.ic_timeline_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_sense_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar_24)!!)
        removedTypeIconList = listOf<Drawable>(ContextCompat.getDrawable(requireContext(), R.drawable.ic_sense_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar_24)!!)
        typeTextList = listOf(getString(R.string.title_timeline), getString(R.string.title_by_sense), getString(R.string.title_by_score), getString(R.string.title_by_calendar))
        removedTypeTextList = listOf(getString(R.string.title_by_sense), getString(R.string.title_by_score), getString(R.string.title_by_calendar))

        if (selectedType!=getString(R.string.title_search_result)) {
            setSpannableText(selectedType, requireContext(), R.color.GY_03, 6, selectedType.length, binding.feedMyTasteTv)   //나의 취향 뒷부분 텍스트 색상 변경
        } else {
            showSearchView(false)
        }

        setMyEventListener()
        observe()

        //기록 목록 조회 API 호출
        viewModel.getPosts(SpfUtils.getIntEncryptedSpf("userId"), 0, "id,desc", null, null, null)
    }

    override fun initAfterBinding() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.feedFcv.findNavController().currentDestination?.id==R.id.searchResultFragment) {
                    binding.feedFcv.findNavController().popBackStack()

                    when (binding.feedFcv.findNavController().currentDestination?.id) {
                        R.id.timelineFragment -> selectedType = "나의 취향 ${getString(R.string.title_timeline)}"
                        R.id.bySenseFragment -> selectedType = "나의 취향 ${getString(R.string.title_by_sense)}"
                        R.id.byScoreFragment -> selectedType = "나의 취향 ${getString(R.string.title_by_score)}"
                        R.id.byCalendarFragment -> selectedType = "나의 취향 ${getString(R.string.title_by_calendar)}"
                    }

                    setSpannableText(selectedType, requireContext(), R.color.GY_03, 6, selectedType.length, binding.feedMyTasteTv)   //나의 취향 뒷부분 텍스트 색상 변경
                    binding.invalidateAll()

                    binding.feedSearchIb.translateX(200, 0f)
                    binding.feedSearchEt.fadeOut(300)
                } else {
                    requireActivity().finish()
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        selectedType = binding.feedMyTasteTv.text.toString()
    }

    private fun setMyEventListener() {
        binding.feedSearchEt.setOnKeyListener { view, keyCode, keyEvent ->
            if(keyCode==KeyEvent.KEYCODE_ENTER && keyEvent.action ==KeyEvent.ACTION_UP){
                (requireActivity() as MainActivity).hideKeyboard(view)

                selectedType = getString(R.string.title_search_result)
                binding.invalidateAll()

                goSearchResultFrag(binding.feedFcv.findNavController().currentDestination?.id)
            }

            false
        }
    }

    private fun changeFragment() {
        when (selectedType) {
            "나의 취향 ${getString(R.string.title_timeline)}" -> binding.feedFcv.findNavController().navigate(R.id.action_global_timelineFragment)
            "나의 취향 ${getString(R.string.title_by_sense)}" -> binding.feedFcv.findNavController().navigate(R.id.action_global_bySenseFragment)
            "나의 취향 ${getString(R.string.title_by_score)}" -> binding.feedFcv.findNavController().navigate(R.id.action_global_byScoreFragment)
            "나의 취향 ${getString(R.string.title_by_calendar)}" -> binding.feedFcv.findNavController().navigate(R.id.action_global_byCalendarFragment)
            else -> goSearchResultFrag(binding.feedFcv.findNavController().currentDestination?.id)
        }
    }

    private fun goSearchResultFrag(currentFrag: Int?) {
        val keyword: String = binding.feedSearchEt.text.toString()
        val action = when (currentFrag) {
            R.id.timelineFragment -> TimelineFragmentDirections.actionTimelineFragmentToSearchResultFragment(keyword)
            R.id.bySenseFragment -> BySenseFragmentDirections.actionBySenseFragmentToSearchResultFragment(keyword)
            R.id.byScoreFragment -> ByScoreFragmentDirections.actionByScoreFragmentToSearchResultFragment(keyword)
            R.id.byCalendarFragment -> ByCalendarFragmentDirections.actionByCalendarFragmentToSearchResultFragment(keyword)
            else -> {
                binding.feedFcv.findNavController().currentBackStackEntry?.savedStateHandle?.set("keyword", keyword)
                null
            }
        }
        action?.let { binding.feedFcv.findNavController().navigate(action) }
    }

    private fun observe() {
        mainVm.changeFragmentFlag.observe(viewLifecycleOwner, Observer {
            val changeFragmentFlag = it.getContentIfNotHandled()

            if (changeFragmentFlag!=null && changeFragmentFlag) {
                changeFragment()
            }
        })

        viewModel.posts.observe(viewLifecycleOwner, Observer {
            val posts = it.getContentIfNotHandled()

            if (posts!=null) {
                if (posts.empty) {
                    binding.feedFcv.findNavController().navigate(R.id.action_global_noFeedFragment)
                } else {
                    changeFragment()
                }
            }
        })
    }

    //나의 취향 터치뷰 클릭 리스너
    fun onClickTypeSelectTouchView(visibility: Int) {
        isTypeSelectShown = visibility==View.GONE
        binding.invalidateAll()
    }

    fun onClickTypeCl(order: Int) {
        isTypeSelectShown = false

        var clickedText: String
        var clickedIcon: Drawable

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

        selectedType = "나의 취향 $clickedText"

        //선택한 타입 텍스트&아이콘 제외하고 나머지를 typeSelectLayout 에 보여주기
        removedTypeTextList = typeTextList.filterNot { it == clickedText }
        removedTypeIconList = typeIconList.filterNot { it == clickedIcon }

        binding.invalidateAll()

        if (binding.feedFcv.findNavController().currentDestination?.id!=R.id.noFeedFragment) {
            changeFragment()
        }
    }

    fun showSearchView(focusable: Boolean) {
        if (binding.feedSearchEt.visibility==View.INVISIBLE) {
            if (binding.feedTypeSelectLayout.visibility==View.VISIBLE) {
                onClickTypeSelectTouchView(binding.feedTypeSelectLayout.visibility)
            }

            binding.feedSearchIb.translateX(300, -(getDeviceWidth() - convertDpToPx(requireContext(), 86)).toFloat())

            binding.feedSearchEt.apply {
                text.clear()
                fadeIn(300)

                if (focusable) {
                    requestFocus()
                    (requireActivity() as MainActivity).showKeyboard(this)
                }
            }
        }
    }
}