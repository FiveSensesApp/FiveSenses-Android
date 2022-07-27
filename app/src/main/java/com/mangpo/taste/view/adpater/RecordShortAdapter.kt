package com.mangpo.taste.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ItemByScoreFilterBinding
import com.mangpo.taste.databinding.ItemBySenseFilterBinding
import com.mangpo.taste.databinding.ItemRecordCntBinding
import com.mangpo.taste.databinding.ItemRecordShortBinding
import com.mangpo.taste.view.model.Record

class RecordShortAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface MyClickListener {
        fun onClick(record: Record)
    }

    private lateinit var bySenseFilterBinding: ItemBySenseFilterBinding
    private lateinit var byScoreFilterBinding: ItemByScoreFilterBinding
    private lateinit var recordCntBinding: ItemRecordCntBinding
    private lateinit var contentBinding: ItemRecordShortBinding
    private lateinit var baseRecords: List<Record>
    private lateinit var records: List<Record>
    private lateinit var myClickListener: MyClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ContentViewType.BY_SENSE_FILTER.num -> {
                bySenseFilterBinding = ItemBySenseFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BySenseFilterViewHolder(bySenseFilterBinding)
            }
            ContentViewType.BY_SCORE_FILTER.num -> {
                byScoreFilterBinding = ItemByScoreFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ByScoreFilterViewHolder(byScoreFilterBinding)
            }
            ContentViewType.RECORD_CNT.num -> {
                recordCntBinding = ItemRecordCntBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecordCntViewHolder(recordCntBinding)
            }
            else -> {
                contentBinding = ItemRecordShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecordShortViewHolder(contentBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (records[position].viewType) {
            ContentViewType.BY_SENSE_FILTER.num -> (holder as BySenseFilterViewHolder).bind()
            ContentViewType.BY_SCORE_FILTER.num -> (holder as ByScoreFilterViewHolder).bind()
            ContentViewType.RECORD_CNT.num -> (holder as RecordCntViewHolder).bind()
            ContentViewType.CONTENT.num -> (holder as RecordShortViewHolder).bind(records[position])
            else -> return
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (records[position].viewType) {
            0 -> ContentViewType.BY_SENSE_FILTER.num
            1 -> ContentViewType.BY_SCORE_FILTER.num
            2 -> ContentViewType.RECORD_CNT.num
            else -> ContentViewType.CONTENT.num
        }
    }

    override fun getItemCount(): Int = records.size

    inner class RecordShortViewHolder(binding: ItemRecordShortBinding): RecyclerView.ViewHolder(binding.root) {
        private val keywordBtn: AppCompatButton = binding.root

        fun bind(record: Record) {
            keywordBtn.text = record.record!!.keyword

            when (record.record.taste) {
                0 -> keywordBtn.setBackgroundResource(R.drawable.ic_sight_stick)
                1 -> keywordBtn.setBackgroundResource(R.drawable.ic_ear_stick)
                2 -> keywordBtn.setBackgroundResource(R.drawable.ic_smell_stick)
                3 -> keywordBtn.setBackgroundResource(R.drawable.ic_taste_stick)
                4 -> keywordBtn.setBackgroundResource(R.drawable.ic_touch_stick)
                5 -> keywordBtn.setBackgroundResource(R.drawable.bg_gy02_12)
            }

            keywordBtn.setOnClickListener {
                myClickListener.onClick(record)
            }
        }
    }

    inner class RecordCntViewHolder(binding: ItemRecordCntBinding): RecyclerView.ViewHolder(binding.root) {
        private val cntTv: TextView = binding.root

        fun bind() {
            cntTv.text = "총 ${records.size-2}개"
        }
    }

    inner class BySenseFilterViewHolder(binding: ItemBySenseFilterBinding): RecyclerView.ViewHolder(binding.root) {
        private val filterRg: RadioGroup = binding.bySenseFilterRg

        fun bind() {
            //라디오그룹 체크 리스너
            filterRg.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.by_sense_filter_sight_rb -> records = baseRecords.filter { it.record==null || it.record?.taste==0 }
                    R.id.by_sense_filter_ear_rb -> records = baseRecords.filter { it.record==null || it.record?.taste==1 }
                    R.id.by_sense_filter_smell_rb -> records = baseRecords.filter { it.record==null || it.record?.taste==2 }
                    R.id.by_sense_filter_taste_rb -> records = baseRecords.filter { it.record==null || it.record?.taste==3 }
                    R.id.by_sense_filter_touch_rb -> records = baseRecords.filter { it.record==null || it.record?.taste==4 }
                    R.id.by_sense_filter_question_rb -> records = baseRecords.filter { it.record==null || it.record?.taste==5 }
                }

                notifyDataSetChanged()
            }
        }
    }

    inner class ByScoreFilterViewHolder(binding: ItemByScoreFilterBinding): RecyclerView.ViewHolder(binding.root) {
        private val filterRg: RadioGroup = binding.root

        fun bind() {
            filterRg.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.by_score_1score_rb -> records = baseRecords.filter { it.record==null || it.record?.star==1f }
                    R.id.by_score_2score_rb -> records = baseRecords.filter { it.record==null || it.record?.star==2f }
                    R.id.by_score_3score_rb -> records = baseRecords.filter { it.record==null || it.record?.star==3f }
                    R.id.by_score_4score_rb -> records = baseRecords.filter { it.record==null || it.record?.star==4f }
                    R.id.by_score_5score_rb -> records = baseRecords.filter { it.record==null || it.record?.star==5f }
                }

                notifyDataSetChanged()
            }
        }
    }

    enum class ContentViewType(val num: Int) {
        BY_SENSE_FILTER(0), BY_SCORE_FILTER(1), RECORD_CNT(2), CONTENT(3)
    }

    fun setData(records: List<Record>, viewType: Int) {
        this.baseRecords = records

        if (viewType==ContentViewType.BY_SENSE_FILTER.num)  //감각별 보관함일 때
            this.records = baseRecords.filter { it.record==null || it.record?.taste==0 }    //디폴트는 시각만 보이도록
        else if (viewType==ContentViewType.BY_SCORE_FILTER.num) //점수별 보관함일 때
            this.records = baseRecords.filter { it.record==null || it.record?.star==5f }    //디폴트는 5점만 보이도록

        notifyDataSetChanged()
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }
}