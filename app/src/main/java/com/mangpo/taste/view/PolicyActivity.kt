package com.mangpo.taste.view

import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityPolicyBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.io.InputStream

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
        try {
            val inputStream: InputStream = resources.openRawResource(raw)
            val b = ByteArray(inputStream.available())
            inputStream.read(b)

            binding.policyContentTv.text = String(b)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}