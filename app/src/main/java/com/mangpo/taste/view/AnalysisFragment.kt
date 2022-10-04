package com.mangpo.taste.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
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
        val legend: Legend = binding.analysisStackedBarchart.legend
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
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

            val floatArray: FloatArray = floatArrayOf(it.percentageOfCategory.SIGHT.toFloat(), it.percentageOfCategory.SMELL.toFloat(), it.percentageOfCategory.HEARING.toFloat(), it.percentageOfCategory.TASTE.toFloat(), it.percentageOfCategory.TOUCH.toFloat(), it.percentageOfCategory.AMBIGUOUS.toFloat())
            val barEntry = BarEntry(0f, floatArray)
            val barDataSet: BarDataSet = BarDataSet(listOf(barEntry), "")
            barDataSet.colors = mutableListOf(R.color.RD_2, R.color.GN_2, R.color.BU_2, R.color.YE_2, R.color.PU_2, R.color.GY_03)
            binding.analysisStackedBarchart.data = BarData(barDataSet)
        })
    }

    fun toSettingActivity() {
        findNavController().navigate(R.id.action_analysisFragment_to_settingActivity)
    }
}