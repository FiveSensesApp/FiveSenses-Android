package com.mangpo.taste.view

import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivitySupportBinding
import com.mangpo.taste.util.readTxtFile
import com.mangpo.taste.view.adpater.SupportersRVAdapter

class SupportActivity : BaseActivity<ActivitySupportBinding>(ActivitySupportBinding::inflate) {
    private val supporters: ArrayList<String> = arrayListOf()

    override fun initAfterBinding() {
        val text = readTxtFile(baseContext, R.raw.supporters)
        supporters.addAll(text!!.split("\n"))

        val supportersRVAdapter: SupportersRVAdapter = SupportersRVAdapter()
        supportersRVAdapter.setSupporters(supporters)
        binding.supportRv.adapter = supportersRVAdapter

        binding.apply {
            activity = this@SupportActivity
            supportersSize = supporters.size
        }
    }
}