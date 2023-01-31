package com.mangpo.taste.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.*
import com.mangpo.taste.view.model.Record

class RecordShortAdapter constructor(private val records: MutableList<Record>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface MyClickListener {
        fun onClick(content: ContentEntity)
        fun changeFilter(filter: String)
        fun changeFilter(filter: Int)
        fun unmarkedDate()
    }

    private lateinit var bySenseFilterBinding: ItemBySenseFilterBinding
    private lateinit var byScoreFilterBinding: ItemByScoreFilterBinding
    private lateinit var recordCntBinding: ItemRecordCntBinding
    private lateinit var contentBinding: ItemRecordShortBinding
    private lateinit var myClickListener: MyClickListener
    private lateinit var recordCntViewHolder: RecordCntViewHolder
    private lateinit var byScoreFilterViewHolder: ByScoreFilterViewHolder

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
                byScoreFilterViewHolder = ByScoreFilterViewHolder(byScoreFilterBinding)
                byScoreFilterViewHolder
            }
            ContentViewType.RECORD_CNT.num -> {
                recordCntBinding = ItemRecordCntBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                recordCntViewHolder = RecordCntViewHolder(recordCntBinding)
                recordCntViewHolder
            }
            else -> {
                contentBinding = ItemRecordShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecordShortViewHolder(contentBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (this.records[position].viewType) {
            ContentViewType.BY_SENSE_FILTER.num -> (holder as BySenseFilterViewHolder).bind()
            ContentViewType.BY_SCORE_FILTER.num -> (holder as ByScoreFilterViewHolder).bind()
            ContentViewType.RECORD_CNT.num -> holder as RecordCntViewHolder
            ContentViewType.CONTENT.num -> (holder as RecordShortViewHolder).bind(this.records[position], position)
            else -> return
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.records[position].viewType) {
            0 -> ContentViewType.BY_SENSE_FILTER.num
            1 -> ContentViewType.BY_SCORE_FILTER.num
            2 -> ContentViewType.RECORD_CNT.num
            else -> ContentViewType.CONTENT.num
        }
    }

    override fun getItemCount(): Int = this.records.size

    inner class RecordShortViewHolder(private val binding: ItemRecordShortBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(record: Record, position: Int) {
            binding.content = record.record
            binding.clickListener = this@RecordShortAdapter.myClickListener

            when (record.record!!.category) {
                "SIGHT" -> binding.background = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_sight_stick)
                "HEARING" -> binding.background = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_ear_stick)
                "SMELL" -> binding.background = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_smell_stick)
                "TASTE" -> binding.background = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_taste_stick)
                "TOUCH" -> binding.background = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_touch_stick)
                "AMBIGUOUS" -> binding.background = ContextCompat.getDrawable(binding.root.context, R.drawable.bg_gy01_12)
            }
        }
    }

    inner class RecordCntViewHolder(binding: ItemRecordCntBinding): RecyclerView.ViewHolder(binding.root) {
        private val cntTv: TextView = binding.root
        private var cnt: Int = 0

        fun setCnt(cnt: Int) {
            this.cnt = cnt
            cntTv.text = "총 ${cnt}개"

            //달력별 취향 보관함일 때 총 개수가 0이 되면 달력에 기록 유무 표시를 지워줘야 함.
            if (cnt==0) {
                myClickListener.unmarkedDate()
            }
        }

        fun getCnt(): Int = this.cnt
    }

    inner class BySenseFilterViewHolder(binding: ItemBySenseFilterBinding): RecyclerView.ViewHolder(binding.root) {
        private val filterRg: RadioGroup = binding.bySenseFilterRg

        fun bind() {
            //라디오그룹 체크 리스너
            filterRg.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.by_sense_filter_sight_rb -> myClickListener.changeFilter("SIGHT")
                    R.id.by_sense_filter_ear_rb -> myClickListener.changeFilter("HEARING")
                    R.id.by_sense_filter_smell_rb -> myClickListener.changeFilter("SMELL")
                    R.id.by_sense_filter_taste_rb -> myClickListener.changeFilter("TASTE")
                    R.id.by_sense_filter_touch_rb -> myClickListener.changeFilter("TOUCH")
                    R.id.by_sense_filter_question_rb -> myClickListener.changeFilter("AMBIGUOUS")
                }
            }
        }
    }

    inner class ByScoreFilterViewHolder(binding: ItemByScoreFilterBinding): RecyclerView.ViewHolder(binding.root) {
        private val filterRg: RadioGroup = binding.byScoreRg

        fun bind() {
            filterRg.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.by_score_1score_rb -> myClickListener.changeFilter(1)
                    R.id.by_score_2score_rb -> myClickListener.changeFilter(2)
                    R.id.by_score_3score_rb -> myClickListener.changeFilter(3)
                    R.id.by_score_4score_rb -> myClickListener.changeFilter(4)
                    R.id.by_score_5score_rb -> myClickListener.changeFilter(5)
                }
            }
        }

        fun getScoreFilter(): Int {
            return when (filterRg.checkedRadioButtonId) {
                R.id.by_score_1score_rb -> 1
                R.id.by_score_2score_rb -> 2
                R.id.by_score_3score_rb -> 3
                R.id.by_score_4score_rb -> 4
                else -> 5
            }
        }
    }

    enum class ContentViewType(val num: Int) {
        BY_SENSE_FILTER(0), BY_SCORE_FILTER(1), RECORD_CNT(2), CONTENT(3)
    }

    private fun mapperToRecord(contentEntities: List<ContentEntity>): List<Record> {
        val records: MutableList<Record> = mutableListOf()

        for (contentEntity in contentEntities) {
            records.add(Record(3, contentEntity))
        }

        return records
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    fun getSenseFilter(): String {
        return if (!::bySenseFilterBinding.isInitialized) {
            "SIGHT"
        } else {
            when (bySenseFilterBinding.bySenseFilterRg.checkedRadioButtonId) {
                bySenseFilterBinding.bySenseFilterSightRb.id -> "SIGHT"
                bySenseFilterBinding.bySenseFilterEarRb.id -> "HEARING"
                bySenseFilterBinding.bySenseFilterSmellRb.id -> "SMELL"
                bySenseFilterBinding.bySenseFilterTasteRb.id -> "TASTE"
                bySenseFilterBinding.bySenseFilterTouchRb.id -> "TOUCH"
                else -> "AMBIGUOUS"
            }
        }
    }

    fun getScoreFilter(): Int {
        return if (!::byScoreFilterBinding.isInitialized) {
            5
        } else {
            byScoreFilterViewHolder.getScoreFilter()
        }
    }

    fun addData(contentEntities: List<ContentEntity>) {
        this.records.addAll(mapperToRecord(contentEntities))
        notifyItemRangeInserted(this.records.size-contentEntities.size, contentEntities.size)
    }
    
    fun clearData() {
        this.records.removeAll { it.viewType==3 }
        notifyDataSetChanged()
    }

    fun removeData(contentId: Int) {
        val position = this.records.indexOf(this.records.find { it.record?.id==contentId })
        this.records.removeAt(position)
        notifyItemRemoved(position) //삭제된 내역 반영

        if (this.records.any { it.viewType==2 }) {
            //(총 개수) - 1
            val cnt = recordCntViewHolder.getCnt() - 1
            recordCntViewHolder.setCnt(cnt)
        }
    }

    fun setCnt(cnt: Int) {
        recordCntViewHolder.setCnt(cnt)
    }

    fun updateData(contentEntity: ContentEntity) {
        val selectedPosition = this.records.indexOf(this.records.find { it.record?.id==contentEntity.id })
        this.records[selectedPosition].record = contentEntity
        notifyItemChanged(selectedPosition)
    }
}