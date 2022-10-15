package com.mangpo.taste.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.taste.databinding.ItemBadgeBinding

class BadgeRVAdapter(): RecyclerView.Adapter<BadgeRVAdapter.BadgeViewHolder>() {
    private var badges: MutableList<GetUserBadgesByUserResEntity> = mutableListOf()

    private lateinit var binding: ItemBadgeBinding
    private lateinit var badgeViewHolder: BadgeViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeRVAdapter.BadgeViewHolder {
        binding = ItemBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        badgeViewHolder = BadgeViewHolder(binding)
        return badgeViewHolder
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        holder.bind(badges[position])
    }

    override fun getItemCount(): Int = badges.size

    inner class BadgeViewHolder(private val binding: ItemBadgeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(badge: GetUserBadgesByUserResEntity) {
            binding.apply {
                this.badge = badge
            }
        }
    }

    fun setBadges(badges: MutableList<GetUserBadgesByUserResEntity>) {
        this.badges = badges
        notifyDataSetChanged()
    }
}