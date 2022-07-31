package com.mangpo.taste.view

import android.content.Intent
import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTimelineBinding
import com.mangpo.taste.view.adpater.RecordDetailAdapter
import com.mangpo.taste.view.model.Record

class TimelineFragment : BaseFragment<FragmentTimelineBinding>(FragmentTimelineBinding::inflate) {
    private val recordEntities: ArrayList<RecordEntity> = arrayListOf<RecordEntity>(
        RecordEntity(
            0,
            0,
            "시각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0f
        ),
        RecordEntity(
            1,
            2,
            "후각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0f
        ),
        RecordEntity(
            2,
            3,
            "미각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0f
        ),
        RecordEntity(
            3,
            4,
            "촉각으로 감각한 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0f
        ),
        RecordEntity(
            4,
            5,
            "모르겠어요 감각 좋아하는 거",
            "50자는 너무 짧은 것 같아~ 아무래도 좋아하는 것에 대해 이야기하는 건 하루종일 할 수 있는데 50자는 하다가 끊기는 느낌? 사실 쓰면서 100자도 짧다고 그건 쩔수 아닐까 싶어",
            "2022.12.23",
            4.0f
        ),
        RecordEntity(5, 0, "시각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0f),
        RecordEntity(6, 1, "청각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0f),
        RecordEntity(7,2, "후각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0f),
        RecordEntity(8, 3, "미각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0f),
        RecordEntity(9, 4, "촉각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0f),
        RecordEntity(10, 5, "모르겠어요 감각 좋아하는 거", null, "2022.12.23", 4.0f)
    )

    private lateinit var recordDetailAdapter: RecordDetailAdapter

    override fun initAfterBinding() {
        initAdapter()
    }

    private fun initAdapter() {
        val records: ArrayList<Record> = arrayListOf(Record(0, null), Record(1, null), Record(2, recordEntities[0]), Record(2, recordEntities[1]), Record(2, recordEntities[2]), Record(2, recordEntities[3]), Record(2, recordEntities[4]), Record(2, recordEntities[5]), Record(2, recordEntities[6]), Record(2, recordEntities[7]), Record(2, recordEntities[8]), Record(2, recordEntities[9]), Record(2, recordEntities[10]))
        recordDetailAdapter = RecordDetailAdapter()
        recordDetailAdapter.setData(records)
        recordDetailAdapter.setMyClickListener(object : RecordDetailAdapter.MyClickListener {
            override fun update(record: RecordEntity) {
                val intent: Intent = Intent(requireContext(), RecordUpdateActivity::class.java)
                intent.putExtra("record", record)
                startActivity(intent)
            }

            override fun delete(recordId: Int) {
            }
        })
        binding.timelineRecordRv.adapter = recordDetailAdapter
    }
}