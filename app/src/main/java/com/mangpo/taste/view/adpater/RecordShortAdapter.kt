package com.mangpo.taste.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ItemByScoreFilterBinding
import com.mangpo.taste.databinding.ItemBySenseFilterBinding
import com.mangpo.taste.databinding.ItemRecordCntBinding
import com.mangpo.taste.databinding.ItemRecordShortBinding
import com.mangpo.taste.view.model.Record

class RecordShortAdapter constructor(private val records: MutableList<Record>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface MyClickListener {
        fun onClick(content: ContentEntity)
        fun changeFilter(filter: String)
    }

    private lateinit var bySenseFilterBinding: ItemBySenseFilterBinding
    private lateinit var byScoreFilterBinding: ItemByScoreFilterBinding
    private lateinit var recordCntBinding: ItemRecordCntBinding
    private lateinit var contentBinding: ItemRecordShortBinding
    private lateinit var baseRecords: MutableList<Record>
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
        when (this.records[position].viewType) {
            ContentViewType.BY_SENSE_FILTER.num -> (holder as BySenseFilterViewHolder).bind()
            ContentViewType.BY_SCORE_FILTER.num -> (holder as ByScoreFilterViewHolder).bind()
            ContentViewType.RECORD_CNT.num -> (holder as RecordCntViewHolder).bind()
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

        fun bind() {
            cntTv.text = "총 ${this@RecordShortAdapter.records.filter { it.viewType==ContentViewType.CONTENT.num }.size}개"
        }
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
        private val filterRg: RadioGroup = binding.root

        fun bind() {
            filterRg.setOnCheckedChangeListener { radioGroup, i ->
                /*when (i) {
                    R.id.by_score_1score_rb -> filteredRecords = baseRecords.filter { it.record==null || it.record?.star==1f } as MutableList<Record>
                    R.id.by_score_2score_rb -> filteredRecords = baseRecords.filter { it.record==null || it.record?.star==2f } as MutableList<Record>
                    R.id.by_score_3score_rb -> filteredRecords = baseRecords.filter { it.record==null || it.record?.star==3f } as MutableList<Record>
                    R.id.by_score_4score_rb -> filteredRecords = baseRecords.filter { it.record==null || it.record?.star==4f } as MutableList<Record>
                    R.id.by_score_5score_rb -> filteredRecords = baseRecords.filter { it.record==null || it.record?.star==5f } as MutableList<Record>
                }*/

                notifyDataSetChanged()
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

    fun setData(records: List<Record>, viewType: Int) {
        this.baseRecords = records as ArrayList<Record>

        /*if (viewType==ContentViewType.BY_SENSE_FILTER.num)  //감각별 보관함일 때
            filteredRecords = baseRecords.filter { it.record==null || it.record?.taste==0 } as MutableList<Record>   //디폴트는 시각만 보이도록
        else if (viewType==ContentViewType.BY_SCORE_FILTER.num) //점수별 보관함일 때
            filteredRecords = baseRecords.filter { it.record==null || it.record?.star==5f } as MutableList<Record>    //디폴트는 5점만 보이도록*/

        notifyDataSetChanged()
    }

    fun setData(records: MutableList<Record>) {
        this.baseRecords = records
//        this.records = records

        notifyDataSetChanged()
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    fun removeItem(position: Int, changedPosition: Int) {
        baseRecords.removeAt(position)
        if (baseRecords!= this.records)   //filteredRecords 가 baseRecords 를 참조하는 경우 발생.
            this.records.removeAt(position)

        notifyItemChanged(changedPosition)    //전체 개수 내용 반영(총 n개)
        notifyItemRangeRemoved(position, this.records.size)  //삭제 내역 반영
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

    fun setCnt(cnt: Int) {
        recordCntBinding.root.text = "총 ${cnt}개"
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
        notifyItemChanged(1)    //전체 개수 내용 반영(총 n개)

        setCnt(this.records.size - 2)
    }
}