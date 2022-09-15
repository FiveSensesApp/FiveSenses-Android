package com.mangpo.taste.view

import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityPolicyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyActivity : BaseActivity<ActivityPolicyBinding>(ActivityPolicyBinding::inflate) {
    override fun initAfterBinding() {
        binding.policyTitleTv.text = intent.getStringExtra("title")

        binding.policyBackIb.setOnClickListener {
            finish()
        }
    }
}