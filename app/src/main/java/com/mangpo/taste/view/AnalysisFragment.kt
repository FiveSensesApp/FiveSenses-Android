package com.mangpo.taste.view

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Legend
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.domain.model.getStat.GetStatResEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentAnalysisBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.adpater.NumOfRecordsTrendVPAdapter
import com.mangpo.taste.view.model.AnalysisUserInfo
import com.mangpo.taste.viewmodel.AnalysisViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AnalysisFragment : BaseFragment<FragmentAnalysisBinding>(FragmentAnalysisBinding::inflate) {
    private val analysisVm: AnalysisViewModel by viewModels()

    private lateinit var numOfRecordsTrendVPAdapter: NumOfRecordsTrendVPAdapter

    lateinit var userInfo: AnalysisUserInfo
    lateinit var getStatResEntity: GetStatResEntity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userInfo = AnalysisUserInfo(SpfUtils.getStrSpf("nickname")!!, SpfUtils.getStrEncryptedSpf("email")!!, SpfUtils.getIntSpf("startDayCnt")!!, SpfUtils.getStrSpf("badgeRepresent"))
        binding.apply {
            fragment = this@AnalysisFragment
            userInfo = this@AnalysisFragment.userInfo
        }

        initTabLayout()
        observe()

        analysisVm.getStat(SpfUtils.getIntEncryptedSpf("userId"))
    }

    override fun initAfterBinding() {
        val legend: Legend = binding.analysisStackedBarchart.legend
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
    }

    private fun initTabLayout() {
        numOfRecordsTrendVPAdapter = NumOfRecordsTrendVPAdapter(this@AnalysisFragment)
        binding.analysisVp.adapter = numOfRecordsTrendVPAdapter

        val tabTitles: Array<String> = arrayOf("일간", "월간")
        TabLayoutMediator(binding.analysisTl, binding.analysisVp) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
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
            binding.invalidateAll()

            if (it.totalPost>=10) {
                val fragment1 = NumOfRecordsTrendGraphFragment()
                val bundle1: Bundle = Bundle()
                bundle1.putParcelableArrayList("dayData", it.countByDayEntities as ArrayList<out Parcelable>)
                fragment1.arguments = bundle1

                val fragment2 = NumOfRecordsTrendGraphFragment()
                val bundle2: Bundle = Bundle()
                bundle2.putParcelableArrayList("monthData", it.countByMonthEntities as ArrayList<out Parcelable>)
                fragment2.arguments = bundle2

                numOfRecordsTrendVPAdapter.setFragments(mutableListOf(fragment1, fragment2))
            }

            /*val floatArray: FloatArray = floatArrayOf(it.percentageOfCategory.SIGHT.toFloat(), it.percentageOfCategory.SMELL.toFloat(), it.percentageOfCategory.HEARING.toFloat(), it.percentageOfCategory.TASTE.toFloat(), it.percentageOfCategory.TOUCH.toFloat(), it.percentageOfCategory.AMBIGUOUS.toFloat())
            val barEntry = BarEntry(0f, floatArray)
            val barDataSet: BarDataSet = BarDataSet(listOf(barEntry), "")
            barDataSet.colors = mutableListOf(R.color.RD_2, R.color.GN_2, R.color.BU_2, R.color.YE_2, R.color.PU_2, R.color.GY_03)
            binding.analysisStackedBarchart.data = BarData(barDataSet)*/
        })
    }

    fun toSettingActivity() {
        findNavController().navigate(R.id.action_analysisFragment_to_settingActivity)
    }
}