package com.mangpo.taste.view

import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseActivity
import com.mangpo.taste.databinding.ActivityPolicyBinding
import com.mangpo.taste.util.readTxtFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyActivity : BaseActivity<ActivityPolicyBinding>(R.layout.activity_policy) {
    override fun initAfterBinding() {
        val title: String = intent.getStringExtra("title")!!
        val content: String? = when (title) {
            getString(R.string.title_ogam_terms) -> readTxtFile(baseContext, R.raw.terms_and_conditions)
            getString(R.string.title_privacy_policy_no_blank) -> readTxtFile(baseContext, R.raw.privacy_policy)
            getString(R.string.title_receive_marketing_information) -> readTxtFile(baseContext, R.raw.receive_marketing_information)
            else -> null
        }

        binding.apply {
            this.activity = this@PolicyActivity
            this.title = title
            this.content = content
        }
    }
}