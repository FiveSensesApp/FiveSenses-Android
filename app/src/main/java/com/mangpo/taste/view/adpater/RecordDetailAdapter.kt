package com.mangpo.taste.view.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ItemByTimelineFilterBinding
import com.mangpo.taste.databinding.ItemRecordCntBinding
import com.mangpo.taste.databinding.ItemRecordDetailBinding
import com.mangpo.taste.util.fadeIn
import com.mangpo.taste.util.fadeOut
import com.mangpo.taste.view.model.Record
import com.mangpo.taste.view.model.RecordDetailResource

class RecordDetailAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface MyClickListener {
        fun update(content: ContentEntity)
        fun delete()
        fun changeSortFilter(sort: String)
    }

    private var deletePostId: Int = -1
    private var updatePostId: Int = -1
    private var selectedPosition: Int = 0
    private var records: MutableList<Record> = mutableListOf(Record(0, null), Record(1, null))

    private lateinit var byTimelineFilterBinding: ItemByTimelineFilterBinding
    private lateinit var recordCntBinding: ItemRecordCntBinding
    private lateinit var contentBinding: ItemRecordDetailBinding
    private lateinit var myClickListener: MyClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ContentViewType.BY_TIMELINE_FILTER.num -> {
                byTimelineFilterBinding = ItemByTimelineFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ByTimelineFilterViewHolder(byTimelineFilterBinding)
            }
            ContentViewType.RECORD_CNT.num -> {
                recordCntBinding = ItemRecordCntBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecordCntViewHolder(recordCntBinding)
            }
            else -> {
                contentBinding = ItemRecordDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecordDetailViewHolder(contentBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (records[position].viewType) {
            ContentViewType.BY_TIMELINE_FILTER.num -> (holder as ByTimelineFilterViewHolder).bind()
            ContentViewType.RECORD_CNT.num -> (holder as RecordCntViewHolder).bind()
            else -> (holder as RecordDetailViewHolder).bind(records[position].record!!, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (records[position].viewType) {
            0 -> ContentViewType.BY_TIMELINE_FILTER.num
            1 -> ContentViewType.RECORD_CNT.num
            else -> ContentViewType.CONTENT.num
        }
    }

    override fun getItemCount(): Int = records.size

    inner class ByTimelineFilterViewHolder(binding: ItemByTimelineFilterBinding): RecyclerView.ViewHolder(binding.root) {
        private val root: RadioGroup = binding.root
        private val latestRb: RadioButton = binding.byTimelineLatestOrderRb
        private val oldestRb: RadioButton = binding.byTimelineOldFashionedRb

        fun bind() {
            root.setOnCheckedChangeListener { radioGroup, i ->
                records.removeIf { it.viewType==2 }
                notifyDataSetChanged()

                when (i) {
                    latestRb.id -> myClickListener.changeSortFilter("desc")
                    oldestRb.id -> myClickListener.changeSortFilter("asc")
                }
            }
        }
    }

    inner class RecordCntViewHolder(binding: ItemRecordCntBinding): RecyclerView.ViewHolder(binding.root) {
        private val cntTv: TextView = binding.root

        fun bind() {
            cntTv.text = "총 0개"
        }
    }

    inner class RecordDetailViewHolder(private val binding: ItemRecordDetailBinding): RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        private val menuCl: ConstraintLayout = binding.recordDetailMenuCl

        fun bind(record: ContentEntity, position: Int) {
            binding.content = record
            binding.adapter = this

            when (record.category) {
                "SIGHT" -> {  //시각
                    binding.resource = RecordDetailResource(ContextCompat.getColor(context, R.color.RD_2), ContextCompat.getDrawable(context, R.drawable.ic_sight_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_rd2_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_rd2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_rd2_23))
                }
                "HEARING" -> {  //청각
                    binding.resource = RecordDetailResource(ContextCompat.getColor(context, R.color.BU_2), ContextCompat.getDrawable(context, R.drawable.ic_ear_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_bu2_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_bu2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_bu2_23))
                }
                "SMELL" -> {  //후각
                    binding.resource = RecordDetailResource(ContextCompat.getColor(context, R.color.GN_3), ContextCompat.getDrawable(context, R.drawable.ic_smell_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_gn3_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_gn2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_gn2_23))
                }
                "TASTE" -> {  //미각
                    binding.resource = RecordDetailResource(ContextCompat.getColor(context, R.color.YE_2), ContextCompat.getDrawable(context, R.drawable.ic_taste_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_ye2_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_ye2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_ye2_23))
                }
                "TOUCH" -> {  //촉각
                    binding.resource = RecordDetailResource(ContextCompat.getColor(context, R.color.PU_2), ContextCompat.getDrawable(context, R.drawable.ic_touch_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_pu2_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_pu2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_pu2_23))
                }
                else -> {   //모르겠어요
                    binding.resource = RecordDetailResource(ContextCompat.getColor(context, R.color.GY_04), ContextCompat.getDrawable(context, R.drawable.ic_question_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_gy04_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_gy04_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_gy04_23))
                }
            }
        }

        fun clickedMoreIv(menuCl: ConstraintLayout) {
            if (menuCl.visibility==View.VISIBLE)
                fadeOut(context, menuCl)
            else
                fadeIn(context, menuCl)
        }

        fun deleteFeed(id: Int) {
            fadeOut(context, menuCl)
            deletePostId = id
            myClickListener.delete()
        }

        fun updateFeed(id: Int) {
            fadeOut(context, menuCl)
            updatePostId = id
            myClickListener.update(this@RecordDetailAdapter.records.find { it.record?.id==id }?.record!!)
        }
    }

    enum class ContentViewType(val num: Int) {
        BY_TIMELINE_FILTER(0), RECORD_CNT(1), CONTENT(2)
    }

    private fun mapperToRecord(contentEntities: List<ContentEntity>): List<Record> {
        val records: MutableList<Record> = mutableListOf()

        for (contentEntity in contentEntities) {
            records.add(Record(2, contentEntity))
        }

        return records
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    fun getSelectedPosition(): Int = selectedPosition

    fun getDeletePostId(): Int = deletePostId

    fun addData(contentEntities: List<ContentEntity>) {
        this.records.addAll(mapperToRecord(contentEntities))
        notifyItemRangeInserted(this.records.size-contentEntities.size, contentEntities.size)
    }

    fun getFilter(): String {
        return if (!::byTimelineFilterBinding.isInitialized) {
            "desc"
        } else {
            if (byTimelineFilterBinding.root.checkedRadioButtonId==byTimelineFilterBinding.byTimelineLatestOrderRb.id) { //최신순
                "desc"
            } else {    //오래된순
                "asc"
            }
        }
    }

    fun getPositionByPostId(postId: Int): Int = records.indexOf(records.find { it.record?.id==postId })

    fun removeData() {
        val position: Int = getPositionByPostId(this.deletePostId)
        this.records.removeAt(position)
        notifyItemRemoved(position) //삭제된 내역 반영
        notifyItemChanged(1)    //전체 개수 내용 반영(총 n개)
    }

    fun clearData() {
        this.records.removeAll { it.viewType==2 }
        notifyDataSetChanged()
    }

    fun updateData(selectedPosition: Int, updatePostResEntity: UpdatePostResEntity) {
        val record: ContentEntity = updatePostResEntity.run {
            ContentEntity(id, category, keyword, star, content, createdDate.split("T")[0])
        }
        this.records[selectedPosition].record = record
        notifyItemChanged(selectedPosition)
    }

    fun getUpdatedPostId(): Int = this.updatePostId
}