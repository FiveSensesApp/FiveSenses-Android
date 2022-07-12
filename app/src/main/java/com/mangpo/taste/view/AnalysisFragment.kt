package com.mangpo.taste.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentAnalysisBinding

class AnalysisFragment : BaseFragment<FragmentAnalysisBinding>(FragmentAnalysisBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        binding.analysisSettingIv.setOnClickListener {
            findNavController().navigate(R.id.action_analysisFragment_to_settingFragment)
        }
    }
}