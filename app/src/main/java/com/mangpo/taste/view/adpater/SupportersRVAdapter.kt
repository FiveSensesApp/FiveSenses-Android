package com.mangpo.taste.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.taste.databinding.ItemSupportersBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.util.getDeviceWidth

class SupportersRVAdapter(): RecyclerView.Adapter<SupportersRVAdapter.SupportersViewHolder>() {
    private val supporters: MutableList<String> = mutableListOf()

    private lateinit var binding: ItemSupportersBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SupportersRVAdapter.SupportersViewHolder {
        binding = ItemSupportersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupportersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SupportersRVAdapter.SupportersViewHolder, position: Int) {
        holder.bind(supporters[position])
    }

    override fun getItemCount(): Int = supporters.size

    inner class SupportersViewHolder(private val binding: ItemSupportersBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(nickname: String) {
            binding.apply {
                this.nickname = nickname
                size = (getDeviceWidth() - convertDpToPx(binding.root.context, 106)) / 3
            }
        }
    }

    fun setSupporters(supporters: MutableList<String>) {
        this.supporters.clear()
        this.supporters.addAll(supporters)
        notifyDataSetChanged()
    }
}