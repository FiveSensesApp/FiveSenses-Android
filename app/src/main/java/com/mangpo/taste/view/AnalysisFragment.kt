package com.mangpo.taste.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentAnalysisBinding

class AnalysisFragment : BaseFragment<FragmentAnalysisBinding>(FragmentAnalysisBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AnalysisFragment", "onViewCreated")
    }

    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        //설정 아이콘 클릭 리스너
        binding.analysisSettingIv.setOnClickListener {
            findNavController().navigate(R.id.action_analysisFragment_to_settingActivity)
        }
    }
}