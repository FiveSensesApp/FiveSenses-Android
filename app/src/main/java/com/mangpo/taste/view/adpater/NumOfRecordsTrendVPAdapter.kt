package com.mangpo.taste.view.adpater

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.domain.model.getStat.CountByDayEntity
import com.mangpo.taste.view.NumOfRecordsTrendGraphFragment

class NumOfRecordsTrendVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private var fragments: MutableList<NumOfRecordsTrendGraphFragment> = arrayListOf()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun setFragments(fragments: MutableList<NumOfRecordsTrendGraphFragment>) {
        this.fragments = fragments
        notifyDataSetChanged()
    }

    /*fun setData(context: Context, data: List<CountByDayEntity>) {
        fragments[0].drawGraph(context, data)
    }*/
}