package com.mangpo.taste.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTimelineBinding
import com.mangpo.taste.view.adpater.RecordDetailAdapter

class TimelineFragment : BaseFragment<FragmentTimelineBinding>(FragmentTimelineBinding::inflate) {
    private val records: ArrayList<RecordEntity> = arrayListOf<RecordEntity>(
        RecordEntity(
            0,
            "시각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0
        ),
        RecordEntity(
            2,
            "후각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0
        ),
        RecordEntity(
            3,
            "미각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0
        ),
        RecordEntity(
            4,
            "촉각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0
        ),
        RecordEntity(
            5,
            "모르겠어요 감각 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0
        ),
        RecordEntity(0, "시각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0),
        RecordEntity(1, "청각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0),
        RecordEntity(2, "후각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0),
        RecordEntity(3, "미각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0),
        RecordEntity(4, "촉각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0),
        RecordEntity(5, "모르겠어요 감각 좋아하는 거", null, "2022.12.23", 4.0)
    )

    private lateinit var recordDetailAdapter: RecordDetailAdapter

    override fun initAfterBinding() {
        binding.timelineTotalCntTv.text = "총 ${records.size}개"
        initAdapter()
    }

    private fun initAdapter() {
        recordDetailAdapter = RecordDetailAdapter()
        recordDetailAdapter.setData(records)
        binding.timelineRecordRv.adapter = recordDetailAdapter
    }
}