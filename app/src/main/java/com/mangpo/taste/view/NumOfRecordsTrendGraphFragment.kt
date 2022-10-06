package com.mangpo.taste.view

import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.mangpo.domain.model.getStat.CountByDayEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentNumOfRecordsTrendGraphBinding
import com.mangpo.taste.util.convertDpToPx


class NumOfRecordsTrendGraphFragment : BaseFragment<FragmentNumOfRecordsTrendGraphBinding>(FragmentNumOfRecordsTrendGraphBinding::inflate) {
    private lateinit var chart: BarChart
    override fun initAfterBinding() {
        chart = binding.trendGraphBarchart

        /*chart.setDrawBarShadow(false)   //그래프 그림자

        chart.setDrawValueAboveBar(false)

        chart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)

//        setData(7, 10f)*/
    }

    private fun initGraph(xAxisValueFormatter: Array<String>, maxValue: Float) {
        /*val barChartRender =
            CustomBarChartRender(
                binding.trendGraphBarchart,
                binding.trendGraphBarchart.animator,
                binding.trendGraphBarchart.viewPortHandler
            )
        barChartRender.setRadius(30)*/

        chart.run {
            setDrawBarShadow(true)
            description.isEnabled = false //차트 옆에 별도로 표기되는 description
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setScaleEnabled(false)  // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            legend.isEnabled = false  //범례 설명 보여주는 기능 비활성화
//            renderer = barChartRender

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

    fun drawGraph(data: List<CountByDayEntity>) {
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
            set1.barShadowColor = ContextCompat.getColor(requireContext(), R.color.GY_01)
            if (data.lastIndex==index) {
                colorList.add(ContextCompat.getColor(requireContext(), R.color.RD_2))
            } else {
                colorList.add(ContextCompat.getColor(requireContext(), R.color.GY_03))
            }
            set1.colors = colorList
            dataSets.add(set1)
        }

        val barData = BarData(dataSets)
        barData.barWidth = 0.4f
        barData.setValueTextColor(Color.TRANSPARENT)

        chart.data = barData

        initGraph(xAxisValueFormat.toTypedArray(), maxValue)
    }

    fun drawStackedBarGraph(data: List<CountByDayEntity>) {
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
    }

    inner class MyXAxisFormatter (private val values: Array<String>) : ValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return values.getOrNull(value.toInt())!!
        }
    }
}