package com.mangpo.taste.view.adpater

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.view.Preview16_9Fragment
import com.mangpo.taste.view.Preview1_1Fragment
import com.mangpo.taste.view.model.PreviewResource

class PreviewVPAdapter(fragmentActivity: FragmentActivity, private val content: ContentEntity, private val resource: PreviewResource): FragmentStateAdapter(fragmentActivity) {
    private val fragments: List<Fragment> = listOf(Preview1_1Fragment(content, resource), Preview16_9Fragment(content, resource))

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun share(position: Int) {
        when (position) {
            0 -> (fragments[position] as Preview1_1Fragment).share()
            1 -> (fragments[position] as Preview16_9Fragment).share()
        }
    }

    fun changeCustomType(position: Int, type: Int) {
        if (position==0) {
            (fragments[position] as Preview1_1Fragment).changeUI(type)
        } else {
            (fragments[position] as Preview16_9Fragment).changeUI(type)
        }
    }

    fun isBgSelected(): Boolean = (fragments[1] as Preview16_9Fragment).isBgSelected()
}