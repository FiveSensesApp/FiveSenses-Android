package com.mangpo.taste.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentAnalysisBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.adpater.NumOfRecordsTrendVPAdapter
import com.mangpo.taste.view.model.AnalysisUserInfo
import com.mangpo.taste.view.model.PercentageOfCategory
import com.mangpo.taste.viewmodel.AnalysisViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalysisFragment : BaseFragment<FragmentAnalysisBinding>(FragmentAnalysisBinding::inflate) {
    private val analysisVm: AnalysisViewModel by viewModels()

    private lateinit var numOfRecordsTrendVPAdapter: NumOfRecordsTrendVPAdapter

    lateinit var userInfo: AnalysisUserInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userInfo = AnalysisUserInfo(SpfUtils.getStrSpf("nickname")!!, SpfUtils.getStrEncryptedSpf("email")!!, SpfUtils.getIntSpf("startDayCnt")!!, SpfUtils.getStrSpf("badgeRepresent"))
        binding.apply {
            fragment = this@AnalysisFragment
            userInfo = this@AnalysisFragment.userInfo
            vm = analysisVm
            lifecycleOwner = viewLifecycleOwner
        }

        initTabLayout()
        observe()

        analysisVm.getBadges(SpfUtils.getIntEncryptedSpf("userId"))
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

            if (it.totalPost>=10) {
                val percentageOfCategories: MutableList<PercentageOfCategory> = arrayListOf()
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_sight), it.percentageOfCategory.SIGHT, ContextCompat.getColor(requireContext(), R.color.RD_2), ContextCompat.getDrawable(requireContext(), R.drawable.ic_sight_character_12)!!))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_smell), it.percentageOfCategory.SMELL, ContextCompat.getColor(requireContext(), R.color.GN), ContextCompat.getDrawable(requireContext(), R.drawable.ic_smell_character_12)!!))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_ear), it.percentageOfCategory.HEARING, ContextCompat.getColor(requireContext(), R.color.BU_2), ContextCompat.getDrawable(requireContext(), R.drawable.ic_ear_character_12)!!))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_taste), it.percentageOfCategory.TASTE, ContextCompat.getColor(requireContext(), R.color.YE_2), ContextCompat.getDrawable(requireContext(), R.drawable.ic_taste_character_12)!!))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_touch), it.percentageOfCategory.TOUCH, ContextCompat.getColor(requireContext(), R.color.PU_2), ContextCompat.getDrawable(requireContext(), R.drawable.ic_touch_character_12)!!))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_etc), it.percentageOfCategory.AMBIGUOUS, ContextCompat.getColor(requireContext(), R.color.GY_03), ContextCompat.getDrawable(requireContext(), R.drawable.ic_question_character_12)!!))
                percentageOfCategories.sortByDescending { it.percentage }
                binding.percentageOfCategory = percentageOfCategories
                drawStackedBarGraph(percentageOfCategories)

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
        })

        binding.invalidateAll()
    }

    private fun drawStackedBarGraph(data: MutableList<PercentageOfCategory>) {
        val values: ArrayList<BarEntry> = ArrayList()
        values.add(BarEntry(0f, data.filter { it.percentage!=0 }.map { it.percentage.toFloat() }.toFloatArray()))

        var set1: BarDataSet = BarDataSet(values, "")
        set1.setDrawIcons(false)
        set1.colors = data.filter { it.percentage!=0 }.map { it.color }

        val barData = BarData(set1)
        barData.setValueFormatter(MyXAxisFormatter())
        barData.setValueTextColor(Color.WHITE)
        barData.setValueTextSize(10f)
        barData.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

        binding.analysisStackedBarchart.data = barData

        initGraph()
    }

    private fun initGraph() {
        binding.analysisStackedBarchart.run {
            setMaxVisibleValueCount(100)
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setScaleEnabled(false)  // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            description.isEnabled = false
            setTouchEnabled(false)
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)
            isHighlightFullBarEnabled = false
            //왼쪽 y축
            axisLeft.run{
                axisMinimum = 0f // 최소값 0
                axisMaximum = 100f
                isEnabled = false
            }
            xAxis.isEnabled = false //x축
            axisRight.isEnabled = false //오른쪽 y축
            legend.isEnabled = false
            minOffset = 0f

            /*val roundedBarChartRenderer = RoundedHorizontalBarChartRenderer(this, animator, viewPortHandler)
            roundedBarChartRenderer.setmRadius(15f)
            renderer = roundedBarChartRenderer*/
            invalidate()    //설정한 것들 반영
        }
    }

    inner class MyXAxisFormatter() : ValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            return "${value.toInt()}%"!!
        }
    }

    fun toSettingActivity() {
        findNavController().navigate(R.id.action_analysisFragment_to_settingActivity)
    }

    fun toUpdateNicknameActivity() {
        findNavController().navigate(R.id.action_analysisFragment_to_updateNicknameActivity)
    }
}