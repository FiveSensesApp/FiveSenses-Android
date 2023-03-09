package com.mangpo.taste.view

import android.view.View
import androidx.core.view.updateLayoutParams
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseNoVMFragment
import com.mangpo.taste.databinding.FragmentOnBoardingBinding
import com.mangpo.taste.util.getDeviceWidth

class OnBoardingFragment(private val mainImg: Int) : BaseNoVMFragment<FragmentOnBoardingBinding>(FragmentOnBoardingBinding::inflate) {
    override fun initAfterBinding() {
        binding.onBoardingMainIv.setImageResource(mainImg)

        when (mainImg) {
            R.drawable.on_boarding1 -> binding.onBoardingLineView.updateLayoutParams { this.width = getDeviceWidth() / 3 }
            R.drawable.on_boarding2 -> binding.onBoardingLineView.updateLayoutParams { this.width = getDeviceWidth() / 3 * 2 }
            R.drawable.on_boarding3 -> binding.onBoardingLineView.updateLayoutParams { this.width = getDeviceWidth() }
            else -> binding.onBoardingLineView.visibility = View.INVISIBLE
        }
    }
}