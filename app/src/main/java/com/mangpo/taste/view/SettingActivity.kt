package com.mangpo.taste.view

import com.mangpo.taste.base.BaseNoVMActivity
import com.mangpo.taste.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseNoVMActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {
    override fun initAfterBinding() {
    }
}