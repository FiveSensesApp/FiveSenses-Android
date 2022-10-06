package com.mangpo.taste.view

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.mangpo.domain.model.getStat.CountByDayEntity
import com.mangpo.domain.model.getStat.CountByMonthEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentNumOfRecordsTrendGraphBinding

class NumOfRecordsTrendGraphFragment() : BaseFragment<FragmentNumOfRecordsTrendGraphBinding>(FragmentNumOfRecordsTrendGraphBinding::inflate) {
    override fun initAfterBinding() {
        showToast(arguments?.getString("TEST", "NULL").toString())
        val dayData = arguments?.getParcelableArrayList<CountByDayEntity>("dayData")
        if (dayData!=null) {
            Log.d("NumOfRecordsTrendGraphFragment", "initAfterBinding")
            drawGraphByDay(requireContext(), dayData)
        }

        val monthData = arguments?.getParcelableArrayList<CountByMonthEntity>("monthData")
        if (monthData!=null) {
            Log.d("NumOfRecordsTrendGraphFragment", "initAfterBinding")
            drawGraphByMonth(requireContext(), monthData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("NumOfRecordsTrendGraphFragment", "onDestroyView")
    }

    private fun initGraph(xAxisValueFormatter: Array<String>, maxValue: Float) {
        binding.trendGraphBarchart.run {
            setDrawBarShadow(true)
            description.isEnabled = false //차트 옆에 별도로 표기되는 description
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setScaleEnabled(false)  // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            legend.isEnabled = false  //범례 설명 보여주는 기능 비활성화

            //왼쪽 y축
            axisLeft.run{
                axisMinimum = 0f // 최소값 0
                axisMaximum = maxValue
                setDrawLabels(false)    //라벨 유무
                setDrawGridLines(false) //격자 라인 활용
                setDrawAxisLine(false) // 축 그리기 설정
            }

            //x축
            xAxis.run {
                setDrawGridLines(false) //격자 라인 비활성화
                setDrawAxisLine(false)  //X축 라인 비활성화
                setPadding(50)
                position = XAxis.XAxisPosition.BOTTOM   //X축을 아래에다가 둔다.
                valueFormatter = MyXAxisFormatter(xAxisValueFormatter)  //Ex. 9/24, 9/26 ...
//                textColor = ContextCompat.getColor(requireContext(), R.color.GY_04) //라벨 텍스트 색상
                textSize = 12f  //라벨 텍스트 크기
            }

            //오른쪽 y축
            axisRight.isEnabled = false

            invalidate()    //설정한 것들 반영
        }
    }

    fun drawGraphByDay(context: Context, data: List<CountByDayEntity>) {
        val values: ArrayList<BarEntry> = ArrayList()
        val xAxisValueFormat: MutableList<String> = mutableListOf()
        val maxValue = data.maxOf { it.count }.toFloat()
        val dataSets: ArrayList<IBarDataSet> = arrayListOf()
        val colorList: ArrayList<Int> = arrayListOf()

        for ((index, d) in data.withIndex()) {
            values.add(BarEntry(index.toFloat(), d.count.toFloat()))
            xAxisValueFormat.add(d.day)

            val set1: BarDataSet = BarDataSet(values, "")
            set1.setDrawIcons(false)
            set1.barShadowColor = ContextCompat.getColor(context, R.color.GY_01)
            if (data.lastIndex==index) {
                colorList.add(ContextCompat.getColor(context, R.color.RD_2))
            } else {
                colorList.add(ContextCompat.getColor(context, R.color.GY_03))
            }
            set1.colors = colorList
            dataSets.add(set1)
        }

        val barData = BarData(dataSets)
        barData.barWidth = 0.4f
        barData.setValueTextColor(Color.TRANSPARENT)

        binding.trendGraphBarchart.data = barData

        initGraph(xAxisValueFormat.toTypedArray(), maxValue)
    }

    fun drawGraphByMonth(context: Context, data: List<CountByMonthEntity>) {
        val values: ArrayList<BarEntry> = ArrayList()
        val xAxisValueFormat: MutableList<String> = mutableListOf()
        val maxValue = data.maxOf { it.count }.toFloat()
        val dataSets: ArrayList<IBarDataSet> = arrayListOf()
        val colorList: ArrayList<Int> = arrayListOf()

        for ((index, d) in data.withIndex()) {
            values.add(BarEntry(index.toFloat(), d.count.toFloat()))
            xAxisValueFormat.add(d.month)

            val set1: BarDataSet = BarDataSet(values, "")
            set1.setDrawIcons(false)
            set1.barShadowColor = ContextCompat.getColor(context, R.color.GY_01)
            if (data.lastIndex==index) {
                colorList.add(ContextCompat.getColor(context, R.color.RD_2))
            } else {
                colorList.add(ContextCompat.getColor(context, R.color.GY_03))
            }
            set1.colors = colorList
            dataSets.add(set1)
        }

        val barData = BarData(dataSets)
        barData.barWidth = 0.4f
        barData.setValueTextColor(Color.TRANSPARENT)

        binding.trendGraphBarchart.data = barData

        initGraph(xAxisValueFormat.toTypedArray(), maxValue)
    }

    /*fun drawStackedBarGraph(data: List<CountByDayEntity>) {
        val values: ArrayList<BarEntry> = ArrayList()
        val xAxisValueFormat: MutableList<String> = mutableListOf()
        val maxValue = data.maxOf { it.count }.toFloat()

        for ((index, d) in data.withIndex()) {
            values.add(BarEntry(index.toFloat(), floatArrayOf(maxValue, d.count.toFloat())))
            xAxisValueFormat.add(d.day)
        }

        val set1: BarDataSet = BarDataSet(values, "")
        set1.setDrawIcons(false)
        set1.valueTextColor = Color.TRANSPARENT
        set1.setColors(ContextCompat.getColor(requireContext(), R.color.GY_01), ContextCompat.getColor(requireContext(), R.color.GY_03))

        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)

        val barData = BarData(dataSets)
        barData.barWidth = 0.55f
        chart.data = barData

        initGraph(xAxisValueFormat.toTypedArray(), maxValue)
    }*/

    inner class MyXAxisFormatter (private val values: Array<String>) : ValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return values.getOrNull(value.toInt())!!
        }
    }
}