package com.mangpo.taste.view

import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {
    override fun initAfterBinding() {
    }
}