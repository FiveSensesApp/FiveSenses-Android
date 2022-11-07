package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityBadgeBinding
import com.mangpo.taste.view.adpater.BadgeRVAdapter
import com.mangpo.taste.viewmodel.BadgeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadgeActivity : BaseActivity<ActivityBadgeBinding>(ActivityBadgeBinding::inflate) {
    private val badgeVm: BadgeViewModel by viewModels()

    private lateinit var badgeInfoBottomSheetFragment: BadgeInfoBottomSheetFragment
    private lateinit var badgeRVAdapter: BadgeRVAdapter
    private lateinit var badges: MutableList<GetUserBadgesByUserResEntity>

    override fun initAfterBinding() {
        badges = (intent.getParcelableArrayExtra("badges")?.toMutableList() as MutableList<GetUserBadgesByUserResEntity>)

        binding.apply {
            activity = this@BadgeActivity
            representativeBadge = intent.getParcelableExtra("representativeBadge")
            size = badges?.filter { !it.isBefore }?.size
        }

        initBottomSheetFragment()
        initAdapter(badges!!)
    }

    private fun initBottomSheetFragment() {
        badgeInfoBottomSheetFragment = BadgeInfoBottomSheetFragment()
        badgeInfoBottomSheetFragment.setEventListener(object : BadgeInfoBottomSheetFragment.EventListener {
            override fun goRecord() {
                val intent: Intent = Intent(this@BadgeActivity, MainActivity::class.java)
                intent.putExtra("goRecord", true)
                setResult(1, intent)
                finish()
            }

            override fun goReview() {
                goUrlPage("https://play.google.com/store/apps/details?id=com.mangpo.taste")
                badgeVm.checkThanks()
                finish()
            }

            override fun changeRepresentativeBadge(badgeId: String) {
                binding.representativeBadge = badges.find { it.id==badgeId }
            }
        })
    }

    private fun initAdapter(badges: MutableList<GetUserBadgesByUserResEntity>) {
        badgeRVAdapter = BadgeRVAdapter()
        badgeRVAdapter.setBadges(badges)
        badgeRVAdapter.setEventListener(object : BadgeRVAdapter.EventListener {
            override fun onClick(badge: GetUserBadgesByUserResEntity) {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("badge", badge)

                badgeInfoBottomSheetFragment.arguments = bundle
                badgeInfoBottomSheetFragment.show(supportFragmentManager, null)
            }
        })

        binding.badgeRv.adapter = badgeRVAdapter
    }
}