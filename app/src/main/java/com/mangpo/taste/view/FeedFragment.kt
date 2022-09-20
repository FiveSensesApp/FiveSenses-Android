package com.mangpo.taste.view

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentFeedBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.setSpannableText
import com.mangpo.taste.viewmodel.FeedViewModel
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate), TextWatcher {
    private val feedVm: FeedViewModel by viewModels()
    private val mainVm: MainViewModel by activityViewModels()
    private val hasRecord: Boolean = true

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var typeIconList: List<Drawable>
    private lateinit var typeTextList: List<String>

    var isTypeSelectShown: Boolean = false
    lateinit var removedTypeTextList: List<String>
    lateinit var removedTypeIconList: List<Drawable>

    override fun initAfterBinding() {
        binding.apply {
            fragment = this@FeedFragment
            mainVm = this@FeedFragment.mainVm
            lifecycleOwner = viewLifecycleOwner
        }

        typeIconList = listOf<Drawable>(ContextCompat.getDrawable(requireContext(), R.drawable.ic_timeline_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_sense_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar_24)!!)
        removedTypeIconList = listOf<Drawable>(ContextCompat.getDrawable(requireContext(), R.drawable.ic_sense_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_24)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar_24)!!)
        typeTextList = listOf(getString(R.string.title_timeline), getString(R.string.title_by_sense), getString(R.string.title_by_score), getString(R.string.title_by_calendar))
        removedTypeTextList = listOf(getString(R.string.title_by_sense), getString(R.string.title_by_score), getString(R.string.title_by_calendar))

        setSpannableText(binding.feedMyTasteTv.text.toString(), requireContext(), R.color.GY_03, 6, binding.feedMyTasteTv.text.length, binding.feedMyTasteTv)   //나의 취향 뒷부분 텍스트 색상 변경

        //뒤로가기 콜백 리스너
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                /*if (binding.feedFcv.findNavController().currentDestination?.id == R.id.searchResultFragment) {  //검색 결과 프래그먼트에 있을 경우
                    binding.feedSearchRightIv.visibility = View.VISIBLE  //오른쪽 검색 아이콘 VISIBLE
                    binding.feedSearchLeftIv.visibility = View.INVISIBLE  //왼쪽 검색 아이콘 INVISIBLE
                    binding.feedSearchEt.visibility = View.INVISIBLE  //검색 EditText INVISIBLE

                    binding.feedSearchEt.text.clear()   //검색 내역 지우기
                } else
                    requireActivity().finish()*/
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)    //뒤로가기 콜백 리스너 등록
        setMyEventListener()
        observe()

        feedVm.getPosts(SpfUtils.getIntEncryptedSpf("userId"), 0, "id,desc", null, null, null)  //기록 목록 조회 API 호출
    }

    override fun onDetach() {
        super.onDetach()

        onBackPressedCallback.remove()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0?.length!! <= 0) {
            showTasteHeader()
            binding.feedFcv.findNavController().popBackStack()
        } else {
            binding.feedMyTasteTv.visibility = View.GONE //나의 취향~ 텍스트뷰 GONE
            binding.feedToggleIv.visibility = View.GONE //토글 이미지뷰 GONE
            binding.feedSearchResultTv.visibility = View.VISIBLE   //검색 결과 텍스트뷰 VISIBLE

            isTypeSelectShown = false
            binding.invalidateAll()

            /*if (binding.feedFcv.findNavController().currentDestination?.id!=R.id.searchResultFragment) {
                val action = NavigationFeedDirections.actionGlobalSearchResultFragment(p0.toString())
                binding.feedFcv.findNavController().navigate(action)
            } else {
                binding.feedFcv.findNavController().previousBackStackEntry?.savedStateHandle?.set("search", p0.toString())
            }*/
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setMyEventListener() {
        //오른쪽 검색 아이콘 클릭 리스너
        binding.feedSearchRightIv.setOnClickListener {
            isTypeSelectShown = false
            binding.invalidateAll()

            it.visibility = View.INVISIBLE  //INVISIBLE

            binding.feedSearchLeftIv.visibility = View.VISIBLE  //왼쪽 검색 아이콘 VISIBLE
            binding.feedSearchEt.visibility = View.VISIBLE  //검색 EditText VISIBLE
        }

        //검색 EditText TextWatcher 등록
        binding.feedSearchEt.addTextChangedListener(this)
    }

    private fun observe() {
        feedVm.posts.observe(viewLifecycleOwner, Observer {
            if (it.empty) { //기록 없을 때
                binding.feedFcv.findNavController().setGraph(R.navigation.navigation_no_feed)
            } else {    //기록 있을 때
                binding.feedFcv.findNavController().setGraph(R.navigation.navigation_feed)
            }
        })

        mainVm.isRecordComplete.observe(viewLifecycleOwner, Observer {
            if (it) {
                feedVm.getPosts(SpfUtils.getIntEncryptedSpf("userId"), 0, "id,desc", null, null, null)  //기록 목록 조회 API 호출
            } else {

            }
        })
    }

    private fun showTasteHeader() {
        binding.feedMyTasteTv.visibility = View.VISIBLE //나의 취향~ 텍스트뷰 VISIBLE
        binding.feedToggleIv.visibility = View.VISIBLE //토글 이미지뷰 VISIBLE
        binding.feedSearchResultTv.visibility = View.GONE   //검색 결과 텍스트뷰 GONE
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

        when (clickedText) {
            getString(R.string.title_timeline) -> binding.feedFcv.findNavController().navigate(R.id.action_global_timelineFragment)
            getString(R.string.title_by_sense) -> binding.feedFcv.findNavController().navigate(R.id.action_global_bySenseFragment)
            getString(R.string.title_by_score) -> binding.feedFcv.findNavController().navigate(R.id.action_global_byScoreFragment)
            getString(R.string.title_by_calendar) -> binding.feedFcv.findNavController().navigate(R.id.action_global_byCalendarFragment)
        }

        binding.feedMyTasteTv.text = "나의 취향 $clickedText"

        //선택한 타입 텍스트&아이콘 제외하고 나머지를 typeSelectLayout 에 보여주기
        removedTypeTextList = typeTextList.filterNot { it == clickedText }
        removedTypeIconList = typeIconList.filterNot { it == clickedIcon }

        binding.invalidateAll()
    }
}