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

class RecordDetailAdapter(records: MutableList<Record>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface MyClickListener {
        fun update(content: ContentEntity)
        fun delete()
        fun share()
        fun changeSortFilter(sort: String)
        fun callGetPostsAPI(filter: String)
    }

    private var deletePostId: Int = -1
    private var updatePostId: Int = -1
    private var sharedPostId: Int = -1
    private var records: MutableList<Record> = records

    private lateinit var byTimelineFilterBinding: ItemByTimelineFilterBinding
    private lateinit var recordCntBinding: ItemRecordCntBinding
    private lateinit var contentBinding: ItemRecordDetailBinding
    private lateinit var myClickListener: MyClickListener
    private lateinit var recordCntViewHolder: RecordCntViewHolder

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
                recordCntViewHolder = RecordCntViewHolder(recordCntBinding)
                recordCntViewHolder
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
        private var cnt: Int = 0

        fun bind() {
            cntTv.text = "총 ${cnt}개"
        }

        fun setCnt(cnt: Int) {
            this.cnt = cnt
            cntTv.text = "총 ${this.cnt}개"
        }

        fun minusCnt() {
            this.cnt -= 1
            cntTv.text = "총 ${this.cnt}개"
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
                menuCl.fadeOut(300)
            else
                menuCl.fadeIn(300)
        }

        fun deleteFeed(id: Int) {
            menuCl.fadeOut(300)
            deletePostId = id
            myClickListener.delete()
        }

        fun updateFeed(id: Int) {
            menuCl.fadeOut(300)
            updatePostId = id
            myClickListener.update(getContentById(id))
        }

        fun shareFeed(id: Int) {
            menuCl.fadeOut(300)
            sharedPostId = id
            myClickListener.share()
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

    private fun getPositionByPostId(postId: Int): Int = records.indexOf(records.find { it.record?.id==postId })

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

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

    fun removeData() {
        val position: Int = getPositionByPostId(this.deletePostId)
        this.records.removeAt(position)

        if (this.records.none { it.viewType == 2 }) {
            myClickListener.callGetPostsAPI(getFilter())
        }

        notifyItemRemoved(position) //삭제된 내역 반영
        recordCntViewHolder.minusCnt()  //전체 개수 - 1
    }

    fun removeData(deletePostId: Int) {
        val position: Int = getPositionByPostId(deletePostId)
        this.records.removeAt(position)
        notifyItemRemoved(position) //삭제된 내역 반영
    }

    fun clearData() {
        this.records.removeAll { it.viewType==2 }
        notifyDataSetChanged()
    }

    fun updateData(updatePostResEntity: UpdatePostResEntity) {
        val record: ContentEntity = updatePostResEntity.run {
            ContentEntity(id, category, keyword, star, content, createdDate.split("T")[0].replace("-", "."))
        }

        val selectedPosition: Int = this.records.indexOf(this.records.find { it.record?.id==record.id })
        this.records[selectedPosition].record = record
        notifyItemChanged(selectedPosition)
    }

    fun setCnt(cnt: Int) {
        this.recordCntViewHolder.setCnt(cnt)
    }

    fun getContentById(id: Int): ContentEntity = this@RecordDetailAdapter.records.find { it.record?.id==id }?.record!!

    fun getSharedPostId(): Int = sharedPostId
}