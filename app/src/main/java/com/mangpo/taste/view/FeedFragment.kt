package com.mangpo.taste.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentFeedBinding

class FeedFragment : BaseFragment<FragmentFeedBinding>(FragmentFeedBinding::inflate) {
    override fun initAfterBinding() {
        //나의 취향 정렬 타입 선택하는 화면에서 paddingLift 설정하기
        binding.feedMyTasteTv.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.feedType1Tv.setPadding(binding.feedMyTasteTv.width, 0, 0, 0)
                binding.feedType2Tv.setPadding(binding.feedMyTasteTv.width, 0, 0, 0)
                binding.feedType3Tv.setPadding(binding.feedMyTasteTv.width, 0, 0, 0)

                binding.feedMyTasteTv.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        setMyEventListener()
    }

    private fun setMyEventListener() {
        //나의 취향 터치뷰 클릭 리스너
        binding.feedTypeSelectTouchView.setOnClickListener {
            if (binding.feedTypeSelectLayout.visibility == View.GONE) { //나의 취향 정렬 타입 선택하는 화면이 안보이고 있으면 보여주기
                binding.feedTypeSelectLayout.visibility = View.VISIBLE
                binding.feedTopLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_wh_bottom30)
            } else {    //나의 취향 정렬 타입 선택하는 화면이 보이고 있으면 숨겨주기
                binding.feedTypeSelectLayout.visibility = View.GONE
                binding.feedTopLayout.background = null
            }
        }
    }
}