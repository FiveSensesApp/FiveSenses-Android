package com.mangpo.taste.view

import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentAnalysisBinding

class AnalysisFragment : BaseFragment<FragmentAnalysisBinding>(FragmentAnalysisBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        //설정 아이콘 클릭 리스너
        binding.analysisSettingIb.setOnClickListener {
            findNavController().navigate(R.id.action_analysisFragment_to_settingActivity)
        }
    }
}