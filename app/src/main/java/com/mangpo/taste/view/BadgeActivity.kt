package com.mangpo.taste.view

import android.os.Bundle
import androidx.navigation.navArgs
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityBadgeBinding
import com.mangpo.taste.view.adpater.BadgeRVAdapter

class BadgeActivity : BaseActivity<ActivityBadgeBinding>(ActivityBadgeBinding::inflate) {
    private val navArgs: BadgeActivityArgs by navArgs()

    private lateinit var badgeRVAdapter: BadgeRVAdapter

    override fun initAfterBinding() {
        val badges = navArgs.badges.toMutableList()

        binding.apply {
            activity = this@BadgeActivity
            representativeBadge = navArgs.representativeBadge
            size = badges.filter { !it.isBefore }.size
        }

        initAdapter(badges)
    }

    private fun initAdapter(badges: MutableList<GetUserBadgesByUserResEntity>) {
        badgeRVAdapter = BadgeRVAdapter()
        badgeRVAdapter.setEventListener(object : BadgeRVAdapter.EventListener {
            override fun onClick(badge: GetUserBadgesByUserResEntity) {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("badge", badge)

                val badgeInfoBottomSheetFragment: BadgeInfoBottomSheetFragment = BadgeInfoBottomSheetFragment()
                badgeInfoBottomSheetFragment.arguments = bundle
                badgeInfoBottomSheetFragment.show(supportFragmentManager, null)
            }
        })

        binding.badgeRv.adapter = badgeRVAdapter
        badgeRVAdapter.setBadges(badges)
    }
}