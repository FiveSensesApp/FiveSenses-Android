package com.mangpo.taste.view

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentNoFeedBinding
import com.mangpo.taste.util.SpfUtils.getStrSpf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoFeedFragment : BaseFragment<FragmentNoFeedBinding>(FragmentNoFeedBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()

        //닉네임 보여주기
        setNicknameUI(getStrSpf("nickname")!!)

        //가이드 버튼 visibility 여부 확인
        /*if (getBooleanSpf("isGuideClicked", false))
            binding.noFeedHowToUseBtn.visibility = View.INVISIBLE
        else
            binding.noFeedHowToUseBtn.visibility = View.VISIBLE*/
    }

    private fun setMyEventListener() {
        //가이드 버튼 클릭 리스너
        binding.noFeedHowToUseBtn.setOnClickListener {
            /*//가이드 노션 페이지로 이동(나중에)
            it.visibility = View.INVISIBLE  //visibility 를 INVISIBLE
            //클릭 여부를 SharedPreferences 에 저장하기
            SpfUtils.writeSpf("isGuideClicked", true)*/
        }
    }

    private fun setNicknameUI(nickname: String) {
        //tryTv 텍스트 특정 부분 색상 GY_04로 변경
        val ssb: SpannableStringBuilder = SpannableStringBuilder("${nickname}님,\n처음으로 취향을 감각해보세요!")
        ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.GY_04)), 0, nickname.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.noFeedTryTv.text = ssb
    }
}