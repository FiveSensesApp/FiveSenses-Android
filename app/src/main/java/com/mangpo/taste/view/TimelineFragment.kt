package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTimelineBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.adpater.RecordDetailAdapter
import com.mangpo.taste.view.model.Record
import com.mangpo.taste.view.model.TwoBtnDialog
import com.mangpo.taste.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimelineFragment : BaseFragment<FragmentTimelineBinding>(FragmentTimelineBinding::inflate) {
    //임시데이터
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
    //임시데이터
    private val records: ArrayList<Record> = arrayListOf(Record(0, null), Record(1, null), Record(2, recordEntities[0]), Record(2, recordEntities[1]), Record(2, recordEntities[2]), Record(2, recordEntities[3]), Record(2, recordEntities[4]), Record(2, recordEntities[5]), Record(2, recordEntities[6]), Record(2, recordEntities[7]), Record(2, recordEntities[8]), Record(2, recordEntities[9]), Record(2, recordEntities[10]))

    private var selectedPosition: Int = 0   //선택한 기록의 아이템 위치를 기록하는 변수

    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var recordDetailAdapter: RecordDetailAdapter

    override fun initAfterBinding() {
        initTwoBtnDialog()
        initAdapter()
    }

    override fun onResume() {
        super.onResume()

        binding.timelineRecordRv.scrollToPosition(selectedPosition) //선택한 기록의 아이템 위치로 이동
    }

    override fun onStop() {
        super.onStop()

        selectedPosition = recordDetailAdapter.getSelectedPosition()    //마지막으로 선택한 기록의 아이템 위치로 저장해놓기
    }

    private fun initTwoBtnDialog() {
        twoBtnDialogFragment = TwoBtnDialogFragment()
        twoBtnDialogFragment.setMyCallback(object : TwoBtnDialogFragment.MyCallback {
            override fun leftAction() {
                selectedPosition = recordDetailAdapter.getSelectedPosition()
                recordEntities.removeAt(selectedPosition - 2)   //이건 나중에 사라질거임.
                records.removeAt(selectedPosition)  //이것도 나중에 사라질 수도(사라질 확률이 높겠다)

                recordDetailAdapter.notifyItemRemoved(selectedPosition) //삭제된 내역 반영
                recordDetailAdapter.notifyItemChanged(1)    //전체 개수 내용 반영(총 n개)
            }

            override fun rightAction() {
            }
        })
    }

    private fun initAdapter() {
        recordDetailAdapter = RecordDetailAdapter()
        recordDetailAdapter.setData(records)
        recordDetailAdapter.setMyClickListener(object : RecordDetailAdapter.MyClickListener {
            override fun update(record: RecordEntity) {
                val intent: Intent = Intent(requireContext(), RecordUpdateActivity::class.java)
                intent.putExtra("record", record)
                startActivity(intent)
            }

            override fun delete(recordId: Int) {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", TwoBtnDialog(getString(R.string.msg_really_delete), getString(R.string.msg_cannot_recover), getString(R.string.action_delete_long), getString(R.string.action_go_back), R.drawable.bg_gy01_12))

                twoBtnDialogFragment.arguments = bundle
                twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
            }
        })

        binding.timelineRecordRv.adapter = recordDetailAdapter
    }
}