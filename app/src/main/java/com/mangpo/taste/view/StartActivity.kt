package com.mangpo.taste.view

import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityStartBinding

class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate) {
    override fun initAfterBinding() {
        binding.startStartBtn.setOnClickListener {
            startNextActivity(MainActivity::class.java)
        }
    }
}