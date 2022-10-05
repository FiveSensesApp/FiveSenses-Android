package com.mangpo.taste.view

import androidx.core.content.ContextCompat
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

    private fun setGraph(xAxisValueFormatter: Array<String>) {
        chart.run {
            description.isEnabled = false //차트 옆에 별도로 표기되는 description
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setScaleEnabled(false)  // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            chart.legend.isEnabled = false  //범례 설명 보여주는 기능 비활성화

            //왼쪽 y축
            axisLeft.run{
                axisMinimum = 0f // 최소값 0
                setDrawLabels(false)    //라벨 유무
                setDrawGridLines(false) //격자 라인 활용
                setDrawAxisLine(false) // 축 그리기 설정
            }

            //x축
            xAxis.run {
                setDrawGridLines(false) //격자 라인 비활성화
                setDrawAxisLine(false)  //X축 라인 비활성화
                position = XAxis.XAxisPosition.BOTTOM   //X축을 아래에다가 둔다.
                valueFormatter = MyXAxisFormatter(xAxisValueFormatter)  //Ex. 9/24, 9/26 ...
                textColor = ContextCompat.getColor(requireContext(), R.color.GY_04) //라벨 텍스트 색상
                textSize = 12f  //라벨 텍스트 크기
            }

            //오른쪽 y축
            axisRight.isEnabled = false


            invalidate()    //설정한 것들 반영
        }
    }

    fun setData(data: List<CountByDayEntity>) {
        val values: ArrayList<BarEntry> = ArrayList()
        val xAxisValueFormat: MutableList<String> = mutableListOf()
        for ((index, d) in data.withIndex()) {
            values.add(BarEntry(index.toFloat(), d.count.toFloat()))
            xAxisValueFormat.add(d.day)
        }
        val set1: BarDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.dataSets[0] as BarDataSet
            set1.values = values
            set1.colors = mutableListOf(ContextCompat.getColor(requireContext(), R.color.GY_01), ContextCompat.getColor(requireContext(), R.color.GY_03))
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "")
            set1.setDrawIcons(false)
            set1.stackLabels = arrayOf("Births", "Divorces")
            set1.valueTextColor = ContextCompat.getColor(requireContext(), R.color.transparent)
            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(set1)
            val barData = BarData(dataSets)
            barData.barWidth = 0.55f
            chart.data = barData

            setGraph(xAxisValueFormat.toTypedArray())
        }
    }

    inner class MyXAxisFormatter (private val values: Array<String>) : ValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return values.getOrNull(value.toInt())!!
        }
    }
}