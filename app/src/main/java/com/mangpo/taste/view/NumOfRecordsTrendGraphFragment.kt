package com.mangpo.taste.view

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.mangpo.domain.model.getStat.CountByDayEntity
import com.mangpo.domain.model.getStat.CountByMonthEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseNoVMFragment
import com.mangpo.taste.databinding.FragmentNumOfRecordsTrendGraphBinding

class NumOfRecordsTrendGraphFragment() : BaseNoVMFragment<FragmentNumOfRecordsTrendGraphBinding>(FragmentNumOfRecordsTrendGraphBinding::inflate) {
    override fun initAfterBinding() {
        val dayData = arguments?.getParcelableArrayList<CountByDayEntity>("dayData")
        if (dayData!=null) {
            drawGraphByDay(requireContext(), dayData)
        }

        val monthData = arguments?.getParcelableArrayList<CountByMonthEntity>("monthData")
        if (monthData!=null) {
            drawGraphByMonth(requireContext(), monthData)
        }

        binding.trendGraphBarchart.setOnChartValueSelectedListener(OnValueSelectedListener(requireContext()))
    }

    private fun initGraph(xAxisValueFormatter: Array<String>, maxValue: Float) {
        binding.trendGraphBarchart.run {
            setDrawBarShadow(true)
            extraTopOffset = 35f
            description.isEnabled = false //차트 옆에 별도로 표기되는 description
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setScaleEnabled(false)  // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            legend.isEnabled = false  //범례 설명 보여주는 기능 비활성화
            marker = MonthlyMarkerView(requireContext(), R.layout.trend_graph_marker_tv)

            //왼쪽 y축
            axisLeft.run{
                setPadding(0, 10, 0, 0)
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
                textSize = 12f  //라벨 텍스트 크기
            }

            //오른쪽 y축
            axisRight.isEnabled = false

            invalidate()    //설정한 것들 반영
        }
    }

    private fun drawGraphByDay(context: Context, data: List<CountByDayEntity>) {
        val values: ArrayList<BarEntry> = ArrayList()
        val xAxisValueFormat: MutableList<String> = mutableListOf()
        val maxValue = data.maxOf { it.count }.toFloat()
        val dataSets: ArrayList<IBarDataSet> = arrayListOf()
        val colorList: ArrayList<Int> = arrayListOf()

        for ((index, d) in data.withIndex()) {
            values.add(BarEntry(index.toFloat(), d.count.toFloat()))
            xAxisValueFormat.add(d.day)

            val set1: BarDataSet = BarDataSet(values, "")
            set1.barShadowColor = ContextCompat.getColor(context, R.color.GY_01)
            if (data.lastIndex==index) {
                colorList.add(ContextCompat.getColor(context, R.color.RD_2))
            } else {
                colorList.add(ContextCompat.getColor(context, R.color.GY_03))
            }
            set1.colors = colorList
            set1.highLightAlpha = 0
            dataSets.add(set1)
        }

        val barData = BarData(dataSets)
        barData.barWidth = 0.4f
        barData.setValueTextColor(Color.TRANSPARENT)
        binding.trendGraphBarchart.data = barData

        binding.trendGraphBarchart.highlightValue((barData.entryCount-1).toFloat(), dataSets.size-1)

        initGraph(xAxisValueFormat.toTypedArray(), maxValue)
    }

    private fun drawGraphByMonth(context: Context, data: List<CountByMonthEntity>) {
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
            set1.highLightAlpha = 0
            dataSets.add(set1)
        }

        val barData = BarData(dataSets)
        barData.barWidth = 0.4f
        barData.setValueTextColor(Color.TRANSPARENT)
        binding.trendGraphBarchart.data = barData

        binding.trendGraphBarchart.highlightValue((barData.entryCount-1).toFloat(), dataSets.size-1)

        initGraph(xAxisValueFormat.toTypedArray(), maxValue)
    }

    inner class MyXAxisFormatter (private val values: Array<String>) : ValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return values.getOrNull(value.toInt())!!
        }
    }

    inner class OnValueSelectedListener(private val context: Context): OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            val colorList: ArrayList<Int> = arrayListOf()
            for (i in 0 until binding.trendGraphBarchart.data.dataSets.size) {
                if (i==e!!.x.toInt()) {
                    colorList.add(ContextCompat.getColor(context, R.color.RD_2))
                } else {
                    colorList.add(ContextCompat.getColor(context, R.color.GY_03))
                }
                (binding.trendGraphBarchart.data.dataSets[i] as BarDataSet).colors = colorList
            }
        }

        override fun onNothingSelected() {
            val colorList: ArrayList<Int> = arrayListOf()
            for (i in 0 until binding.trendGraphBarchart.data.dataSets.size) {
                colorList.add(ContextCompat.getColor(context, R.color.GY_03))
                (binding.trendGraphBarchart.data.dataSets[i] as BarDataSet).colors = colorList
            }
        }

    }

    inner class MonthlyMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {
        private var tvContent: TextView = findViewById(R.id.trend_graph_marker_tv)

        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            tvContent.text = "${e?.y?.toInt()}개"

            super.refreshContent(e, highlight)
        }

        override fun getOffset(): MPPointF {
            return MPPointF((-(width / 2)).toFloat(), (-binding.trendGraphBarchart.height).toFloat())
        }
    }
}