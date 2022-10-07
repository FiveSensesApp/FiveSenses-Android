package com.mangpo.taste.view.adpater

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.taste.view.NumOfRecordsTrendGraphFragment

class NumOfRecordsTrendVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private var fragments: MutableList<NumOfRecordsTrendGraphFragment> = arrayListOf()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun setFragments(fragments: MutableList<NumOfRecordsTrendGraphFragment>) {
        this.fragments = fragments
        notifyDataSetChanged()
    }
}