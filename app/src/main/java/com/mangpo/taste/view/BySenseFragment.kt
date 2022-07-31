package com.mangpo.taste.view

import androidx.navigation.fragment.findNavController
import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentBySenseBinding
import com.mangpo.taste.view.adpater.RecordShortAdapter
import com.mangpo.taste.view.model.Record

class BySenseFragment : BaseFragment<FragmentBySenseBinding>(FragmentBySenseBinding::inflate) {
    private lateinit var recordShortAdapter: RecordShortAdapter

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
        RecordEntity(7, 2, "후각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0f),
        RecordEntity(8, 3, "미각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0f),
        RecordEntity(9, 4, "촉각으로 감각한 좋아하는 거", null, "2022.12.23", 4.0f),
        RecordEntity(10, 5, "모르겠어요 감각 좋아하는 거", null, "2022.12.23", 4.0f)
    )

    override fun initAfterBinding() {
        initAdapter()
    }

    private fun initAdapter() {
        val records: ArrayList<Record> = arrayListOf(Record(0, null), Record(2, null), Record(3, recordEntities[0]), Record(3, recordEntities[1]), Record(3, recordEntities[2]), Record(3, recordEntities[3]), Record(3, recordEntities[4]), Record(3, recordEntities[5]), Record(3, recordEntities[6]), Record(3, recordEntities[7]), Record(3, recordEntities[8]), Record(3, recordEntities[9]), Record(3, recordEntities[10]))
        recordShortAdapter = RecordShortAdapter()
        recordShortAdapter.setMyClickListener(object : RecordShortAdapter.MyClickListener {
            override fun onClick(record: Record) {
                val action = BySenseFragmentDirections.actionGlobalRecordDialogFragment(record)
                findNavController().navigate(action)
            }
        })
        recordShortAdapter.setData(records, 0)
        binding.bySenseRv.adapter = recordShortAdapter
    }
}