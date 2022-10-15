package com.mangpo.taste.view

import androidx.navigation.navArgs
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityBadgeBinding
import com.mangpo.taste.view.adpater.BadgeRVAdapter

class BadgeActivity : BaseActivity<ActivityBadgeBinding>(ActivityBadgeBinding::inflate) {
    private val navArgs: BadgeActivityArgs by navArgs()

    private lateinit var badgeRVAdapter: BadgeRVAdapter

    override fun initAfterBinding() {
        binding.apply {
            activity = this@BadgeActivity
            representativeBadge = navArgs.representativeBadge
        }

        initAdapter(navArgs.badges.toMutableList())
    }

    private fun initAdapter(badges: MutableList<GetUserBadgesByUserResEntity>) {
        badgeRVAdapter = BadgeRVAdapter()
        binding.badgeRv.adapter = badgeRVAdapter
        badgeRVAdapter.setBadges(badges)
    }
}