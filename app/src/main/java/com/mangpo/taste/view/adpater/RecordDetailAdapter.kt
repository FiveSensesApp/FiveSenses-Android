package com.mangpo.taste.view.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ItemByTimelineFilterBinding
import com.mangpo.taste.databinding.ItemRecordCntBinding
import com.mangpo.taste.databinding.ItemRecordDetailBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.util.fadeIn
import com.mangpo.taste.util.fadeOut
import com.mangpo.taste.view.model.Record
import com.willy.ratingbar.BaseRatingBar

class RecordDetailAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface MyClickListener {
        fun update(record: RecordEntity)
        fun delete(recordId: Int)
    }

    private lateinit var byTimelineFilterBinding: ItemByTimelineFilterBinding
    private lateinit var recordCntBinding: ItemRecordCntBinding
    private lateinit var contentBinding: ItemRecordDetailBinding
    private lateinit var records: List<Record>
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
            ContentViewType.BY_TIMELINE_FILTER.num -> holder as ByTimelineFilterViewHolder
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

    }

    inner class RecordCntViewHolder(binding: ItemRecordCntBinding): RecyclerView.ViewHolder(binding.root) {
        private val cntTv: TextView = binding.root

        fun bind() {
            cntTv.text = "총 ${records.size-2}개"
        }
    }

    inner class RecordDetailViewHolder(binding: ItemRecordDetailBinding): RecyclerView.ViewHolder(binding.root) {
        private val root: ConstraintLayout = binding.root
        private val moreIv: ImageView = binding.recordDetailMoreIv
        private val characterIv: ImageView = binding.recordDetailCharacterIv
        private val keywordTv: TextView = binding.recordDetailKeywordTv
        private val contentTv: TextView = binding.recordDetailContentTv
        private val dateTv: TextView = binding.recordDetailDateTv
        private val starSrb: BaseRatingBar = binding.recordDetailSrb
        private val menuCl: ConstraintLayout = binding.recordDetailMenuCl
        private val updateClickView: View = binding.recordDetailUpdateClickView

        fun bind(record: RecordEntity, position: Int) {
            keywordTv.text = record.keyword

            if (record.content==null)
                contentTv.visibility = View.GONE
            else {
                contentTv.visibility = View.VISIBLE
                contentTv.text = record.content
            }

            dateTv.text = record.date

            starSrb.rating = record.star

            when (record.taste) {
                0 -> {  //시각
                    dateTv.setTextColor(ContextCompat.getColor(root.context, R.color.RD_2))
                    moreIv.setImageResource(R.drawable.ic_more_rd2_44)
                    characterIv.setImageResource(R.drawable.ic_sight_character_72)
                    starSrb.setFilledDrawableRes(R.drawable.ic_star_fill_rd2_23)
                    starSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_rd2_23)
                }
                1 -> {  //청각
                    dateTv.setTextColor(ContextCompat.getColor(root.context, R.color.BU_2))
                    moreIv.setImageResource(R.drawable.ic_more_bu2_44)
                    characterIv.setImageResource(R.drawable.ic_ear_character_72)
                    starSrb.setFilledDrawableRes(R.drawable.ic_star_fill_bu2_23)
                    starSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_bu2_23)
                }
                2 -> {  //후각
                    dateTv.setTextColor(ContextCompat.getColor(root.context, R.color.GN_3))
                    moreIv.setImageResource(R.drawable.ic_more_gn3_44)
                    characterIv.setImageResource(R.drawable.ic_smell_character_72)
                    starSrb.setFilledDrawableRes(R.drawable.ic_star_fill_gn2_23)
                    starSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_gn2_23)
                }
                3 -> {  //미각
                    dateTv.setTextColor(ContextCompat.getColor(root.context, R.color.YE_2))
                    moreIv.setImageResource(R.drawable.ic_more_ye2_44)
                    characterIv.setImageResource(R.drawable.ic_taste_character_72)
                    starSrb.setFilledDrawableRes(R.drawable.ic_star_fill_ye2_23)
                    starSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_ye2_23)
                }
                4 -> {  //촉각
                    dateTv.setTextColor(ContextCompat.getColor(root.context, R.color.PU_2))
                    moreIv.setImageResource(R.drawable.ic_more_pu2_44)
                    characterIv.setImageResource(R.drawable.ic_touch_character_72)
                    starSrb.setFilledDrawableRes(R.drawable.ic_star_fill_pu2_23)
                    starSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_pu2_23)
                }
                else -> {   //모르겠어요
                    dateTv.setTextColor(ContextCompat.getColor(root.context, R.color.GY_04))
                    moreIv.setImageResource(R.drawable.ic_more_gy04_44)
                    characterIv.setImageResource(R.drawable.ic_question_character_72)
                    starSrb.setFilledDrawableRes(R.drawable.ic_star_fill_gy04_23)
                    starSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_gy04_23)
                }
            }

            if (position == records.size-1)
                root.setPadding(convertDpToPx(root.context, 20), 0, convertDpToPx(root.context, 20), convertDpToPx(root.context, 62))
            else
                root.setPadding(convertDpToPx(root.context, 20), 0, convertDpToPx(root.context, 20), convertDpToPx(root.context, 16))

            moreIv.setOnClickListener {
                if (menuCl.visibility==View.VISIBLE)
                    fadeOut(root.context, menuCl)
                else
                    fadeIn(root.context, menuCl)
            }

            updateClickView.setOnClickListener {    //수정 클릭뷰 클릭 리스너
                myClickListener.update(record)
            }
        }
    }

    enum class ContentViewType(val num: Int) {
        BY_TIMELINE_FILTER(0), RECORD_CNT(1), CONTENT(2)
    }

    fun setData(records: List<Record>) {
        this.records = records
        notifyDataSetChanged()
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }
}