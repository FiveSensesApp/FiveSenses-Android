package com.mangpo.taste.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mangpo.domain.model.getStat.GetStatResEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentAnalysisBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.model.AnalysisUserInfo
import com.mangpo.taste.viewmodel.AnalysisViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalysisFragment : BaseFragment<FragmentAnalysisBinding>(FragmentAnalysisBinding::inflate) {
    private val analysisVm: AnalysisViewModel by viewModels()

    lateinit var userInfo: AnalysisUserInfo
    lateinit var getStatResEntity: GetStatResEntity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userInfo = AnalysisUserInfo(SpfUtils.getStrSpf("nickname")!!, SpfUtils.getStrEncryptedSpf("email")!!, SpfUtils.getIntSpf("startDayCnt")!!)
        binding.apply {
            fragment = this@AnalysisFragment
            userInfo = this@AnalysisFragment.userInfo
        }

        observe()

        analysisVm.getStat(SpfUtils.getIntEncryptedSpf("userId"))
    }

    override fun initAfterBinding() {
    }

    private fun observe() {
        analysisVm.toast.observe(viewLifecycleOwner, Observer {
            val toast = it.getContentIfNotHandled()

            if (toast!=null) {
                showToast(toast)
            }
        })

        analysisVm.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        })

        analysisVm.getStatResEntity.observe(viewLifecycleOwner, Observer {
            binding.stat = it
        })
    }

    fun toSettingActivity() {
        findNavController().navigate(R.id.action_analysisFragment_to_settingActivity)
    }
}