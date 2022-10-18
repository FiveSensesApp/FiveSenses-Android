package com.mangpo.taste.view.adpater

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.taste.R
import com.mangpo.taste.view.BannerFragment

class AnalysisBannerVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val bannerFragment: BannerFragment = BannerFragment()
        val bundle: Bundle = Bundle()

        when(position) {
            0 -> bundle.putInt("image",  R.drawable.ic_banner1)
            1 -> bundle.putInt("image",  R.drawable.ic_banner2)
            2 -> bundle.putInt("image",  R.drawable.ic_banner3)
        }

        bannerFragment.arguments = bundle

        return bannerFragment
    }
}