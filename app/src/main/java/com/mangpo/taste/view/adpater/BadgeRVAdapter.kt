package com.mangpo.taste.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ItemBadgeBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.util.getDeviceWidth

class BadgeRVAdapter(): RecyclerView.Adapter<BadgeRVAdapter.BadgeViewHolder>() {
    interface EventListener {
        fun onClick(badge: GetUserBadgesByUserResEntity)
    }

    private var badges: MutableList<GetUserBadgesByUserResEntity> = mutableListOf()

    private lateinit var badgeViewHolder: BadgeViewHolder
    private lateinit var eventListener: EventListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeRVAdapter.BadgeViewHolder {
        val binding = DataBindingUtil.inflate<ItemBadgeBinding>(LayoutInflater.from(parent.context), R.layout.item_badge, parent, false)
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
                this.size = (getDeviceWidth() - convertDpToPx(binding.root.context, 48)) / 3
                this.viewHolder = this@BadgeViewHolder
            }
        }

        fun click(badge: GetUserBadgesByUserResEntity) {
            eventListener.onClick(badge)
        }
    }

    fun setBadges(badges: MutableList<GetUserBadgesByUserResEntity>) {
        this.badges = badges
        notifyDataSetChanged()
    }

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }
}