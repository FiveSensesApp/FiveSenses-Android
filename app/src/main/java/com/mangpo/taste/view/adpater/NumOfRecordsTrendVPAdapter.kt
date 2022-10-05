package com.mangpo.taste.view.adpater

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.domain.model.getStat.CountByDayEntity
import com.mangpo.taste.view.NumOfRecordsTrendGraphFragment

class NumOfRecordsTrendVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val fragments: List<Fragment> = listOf(NumOfRecordsTrendGraphFragment(), NumOfRecordsTrendGraphFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun setData(data: List<CountByDayEntity>) {
        (fragments[0] as NumOfRecordsTrendGraphFragment).setData(data)
    }
}