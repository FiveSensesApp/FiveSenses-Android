package com.mangpo.taste.view

import androidx.fragment.app.viewModels
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentBannerBinding
import com.mangpo.taste.viewmodel.BadgeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BannerFragment() : BaseFragment<FragmentBannerBinding>(FragmentBannerBinding::inflate) {
    private val badgeVm: BadgeViewModel by viewModels()

    override fun initAfterBinding() {
        binding.root.setImageResource(arguments?.getInt("image")!!)

        binding.root.setOnClickListener {
            when (arguments?.getInt("image")!!) {
                R.drawable.ic_banner1 -> goUrlPage("https://www.notion.so/5gaam/5gaam-3b45d6083ad044ab869f0df6378933de")
                R.drawable.ic_banner2 -> {
                    goUrlPage("https://play.google.com/store/apps/details?id=com.mangpo.taste")
                    badgeVm.checkThanks()
                }
                R.drawable.ic_banner3 -> goUrlPage("https://www.instagram.com/5gaam_app")
            }
        }
    }
}