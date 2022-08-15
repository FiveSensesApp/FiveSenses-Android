package com.mangpo.taste.view

import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentOnBoardingBinding

class OnBoardingFragment(private val msg: String, private val img: Int) : BaseFragment<FragmentOnBoardingBinding>(FragmentOnBoardingBinding::inflate) {
    override fun initAfterBinding() {
        binding.onBoardingTv.text = msg
        binding.onBoardingIv.setImageResource(img)
    }
}