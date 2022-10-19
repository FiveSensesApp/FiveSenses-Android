package com.mangpo.taste.view

import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityPolicyBinding
import com.mangpo.taste.util.readTxtFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyActivity : BaseActivity<ActivityPolicyBinding>(ActivityPolicyBinding::inflate) {
    override fun initAfterBinding() {
        val title: String = intent.getStringExtra("title")!!
        binding.policyTitleTv.text = title

        when (title) {
            getString(R.string.title_ogam_terms) -> setText(R.raw.terms_and_conditions)
            getString(R.string.title_privacy_policy_no_blank) -> setText(R.raw.privacy_policy)
            getString(R.string.title_receive_marketing_information) -> setText(R.raw.receive_marketing_information)
        }

        binding.policyBackIb.setOnClickListener {
            finish()
        }
    }

    private fun setText(raw: Int) {
        val text = readTxtFile(baseContext, raw)
        if (text!=null)
            binding.policyContentTv.text = text
    }
}