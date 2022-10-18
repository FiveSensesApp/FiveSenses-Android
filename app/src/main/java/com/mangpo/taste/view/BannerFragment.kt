package com.mangpo.taste.view

import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentBannerBinding

class BannerFragment() : BaseFragment<FragmentBannerBinding>(FragmentBannerBinding::inflate) {
    override fun initAfterBinding() {
        binding.root.setImageResource(arguments?.getInt("image")!!)
    }
}