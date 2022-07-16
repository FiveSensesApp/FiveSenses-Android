package com.mangpo.taste.view

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentOgamSelectBinding
import com.mangpo.taste.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class OgamSelectFragment : BaseFragment<FragmentOgamSelectBinding>(FragmentOgamSelectBinding::inflate) {
    private val mainViewModel: MainViewModel by activityViewModels()

    private val slogans: List<Int> = listOf(R.string.title_slogan1, R.string.title_slogan2, R.string.title_slogan3, R.string.title_slogan4, R.string.title_slogan5, R.string.title_slogan6)

    override fun initAfterBinding() {
        setMyEventListener()
        observe()

        //현재 날짜 보여주기
        val current = System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("M.d (E) a HH:mm", Locale.KOREA).format(current)
        binding.ogamSelectDateTv.text = simpleDateFormat

        //hello 텍스트뷰 부분 텍스트 색상 변경
        val nickname = "워니버니"
        val ssb: SpannableStringBuilder = SpannableStringBuilder("${nickname}${getString(R.string.msg_hello)}")
        ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.GY_04)), 0, nickname.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.ogamSelectHelloTv.text = ssb
    }

    private fun setMyEventListener() {
        //시각 터치뷰 클릭 리스너
        binding.ogamSelectSightTouchView.setOnClickListener(TouchViewListener())
        //청각 터치뷰 클릭 리스너
        binding.ogamSelectEarTouchView.setOnClickListener(TouchViewListener())
        //후각 터치뷰 클릭 리스너
        binding.ogamSelectSmellTouchView.setOnClickListener(TouchViewListener())
        //미각 터치뷰 클릭 리스너
        binding.ogamSelectTasteTouchView.setOnClickListener(TouchViewListener())
        //촉각 터치뷰 클릭 리스너
        binding.ogamSelectTouchTouchView.setOnClickListener(TouchViewListener())
        //모르겠어요 터치뷰 클릭 리스너
        binding.ogamSelectQuestionTouchView.setOnClickListener(TouchViewListener())

        //FloatingActionButton 클릭 리스너 -> MainActivity onBackPressed
        binding.ogamSelectFab.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun observe() {
        //OgamSelectFragment 의 슬로건 idx Observer
        mainViewModel.randomSloganIdx.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.ogamSelectSloganTv.text = getString(slogans[it])
        })
    }

    //터치뷰 클릭리스너 이너 클래스
    inner class TouchViewListener: View.OnClickListener {
        private lateinit var action: NavDirections

        override fun onClick(v: View?) {
            when (v?.id) {
                binding.ogamSelectSightTouchView.id -> action = OgamSelectFragmentDirections.actionOgamSelectFragmentToTasteRecordFragment(R.string.title_sight)
                binding.ogamSelectEarTouchView.id -> action = OgamSelectFragmentDirections.actionOgamSelectFragmentToTasteRecordFragment(R.string.title_ear)
                binding.ogamSelectSmellTouchView.id -> action = OgamSelectFragmentDirections.actionOgamSelectFragmentToTasteRecordFragment(R.string.title_smell)
                binding.ogamSelectTasteTouchView.id -> action = OgamSelectFragmentDirections.actionOgamSelectFragmentToTasteRecordFragment(R.string.title_taste)
                binding.ogamSelectTouchTouchView.id -> action = OgamSelectFragmentDirections.actionOgamSelectFragmentToTasteRecordFragment(R.string.title_touch)
                binding.ogamSelectQuestionTouchView.id -> action = OgamSelectFragmentDirections.actionOgamSelectFragmentToTasteRecordFragment(R.string.title_sense)
            }

            findNavController().navigate(action)
        }
    }
}