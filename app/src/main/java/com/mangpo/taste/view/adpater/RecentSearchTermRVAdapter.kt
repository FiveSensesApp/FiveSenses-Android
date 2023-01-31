package com.mangpo.taste.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.taste.databinding.ItemRecentSearchTermBinding

class RecentSearchTermRVAdapter(): RecyclerView.Adapter<RecentSearchTermRVAdapter.RecentSearchTermViewHolder>() {
    interface MyEventListener {
        fun onSearch(keyword: String)
        fun onDelete(position: Int)
    }

    private val recentSearchTerms: MutableList<String> = mutableListOf()

    private lateinit var binding: ItemRecentSearchTermBinding
    private lateinit var myEventListener: MyEventListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentSearchTermRVAdapter.RecentSearchTermViewHolder {
        binding = ItemRecentSearchTermBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentSearchTermViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentSearchTermRVAdapter.RecentSearchTermViewHolder, position: Int) {
        holder.bind(recentSearchTerms[position], position)
    }

    override fun getItemCount(): Int = recentSearchTerms.size

    inner class RecentSearchTermViewHolder(private val binding: ItemRecentSearchTermBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: String, position: Int) {
            binding.apply {
                vh = this@RecentSearchTermViewHolder
                this.keyword = keyword
                this.position = position
            }
        }

        fun onClick(keyword: String) {
            myEventListener.onSearch(keyword)
        }

        fun onDelete(position: Int) {
            myEventListener.onDelete(position)
            removeData(position)
        }
    }

    fun setMyEventListener(myEventListener: MyEventListener) {
        this.myEventListener = myEventListener
    }

    fun setRecentSearchTerm(recentSearchTerms: MutableList<String>) {
        this.recentSearchTerms.apply {
            clear()
            addAll(recentSearchTerms)
        }
        notifyDataSetChanged()
    }

    private fun removeData(position: Int) {
        this.recentSearchTerms.removeAt(position)
        notifyItemRemoved(position) //삭제된 내역 반영
    }

    fun clearData() {
        this.recentSearchTerms.clear()
        notifyDataSetChanged()
    }
}