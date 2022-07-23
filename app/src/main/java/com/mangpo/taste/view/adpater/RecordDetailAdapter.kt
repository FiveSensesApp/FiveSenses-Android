package com.mangpo.taste.view.adpater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ItemRecordDetailBinding

class RecordDetailAdapter(): RecyclerView.Adapter<RecordDetailAdapter.RecordDetailViewHolder>() {
    private lateinit var binding: ItemRecordDetailBinding
    private lateinit var records: List<RecordEntity>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordDetailAdapter.RecordDetailViewHolder {
        binding = ItemRecordDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecordDetailViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecordDetailAdapter.RecordDetailViewHolder,
        position: Int
    ) {
        holder.bind(records[position])
    }

    override fun getItemCount(): Int = records.size

    inner class RecordDetailViewHolder(binding: ItemRecordDetailBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordEntity) {
            binding.recordDetailKeywordTv.text = record.keyword

            if (record.content==null) {
                Log.d("RecordDetailViewHolder", "NULL")
                binding.recordDetailContentTv.visibility = View.GONE
            }else {
                Log.d("RecordDetailViewHolder", "NOT NULL")
                binding.recordDetailContentTv.visibility = View.VISIBLE
                binding.recordDetailContentTv.text = record.content
            }

            binding.recordDetailDateTv.text = record.date

            when (record.taste) {
                0 -> {  //시각
                    binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.RD_2))
                }
                1 -> {  //청각
                    binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.BU_2))
                }
                2 -> {  //후각
                    binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.GN_3))
                }
                3 -> {  //미각
                    binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.YE_2))
                }
                4 -> {  //촉각
                    binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.PU_2))
                }
                else -> {   //모르겠어요
                    binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(binding.root.context, R.color.GY_04))
                }
            }
        }
    }

    fun setData(records: List<RecordEntity>) {
        this.records = records
        notifyDataSetChanged()
    }
}