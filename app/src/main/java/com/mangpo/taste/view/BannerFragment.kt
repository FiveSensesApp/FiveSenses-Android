package com.mangpo.taste.view

import android.content.Intent
import android.net.Uri
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentBannerBinding

class BannerFragment() : BaseFragment<FragmentBannerBinding>(FragmentBannerBinding::inflate) {
    override fun initAfterBinding() {
        binding.root.setImageResource(arguments?.getInt("image")!!)

        binding.root.setOnClickListener {
            when (arguments?.getInt("image")!!) {
                R.drawable.ic_banner1 -> openLink("https://www.notion.so/5gaam/5gaam-3b45d6083ad044ab869f0df6378933de")
                R.drawable.ic_banner2 -> {

                }
                R.drawable.ic_banner3 -> openLink("https://www.instagram.com/5gaam_app")
            }
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}