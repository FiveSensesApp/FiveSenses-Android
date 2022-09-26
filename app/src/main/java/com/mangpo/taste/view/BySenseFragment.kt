package com.mangpo.taste.view

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentBySenseBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.adpater.RecordShortAdapter
import com.mangpo.taste.view.model.Record
import com.mangpo.taste.viewmodel.FeedViewModel
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BySenseFragment : BaseFragment<FragmentBySenseBinding>(FragmentBySenseBinding::inflate) {
    private val mainVm: MainViewModel by activityViewModels()
    private val feedVm: FeedViewModel by viewModels()

    private var page: Int = 0
    private var isLast: Boolean = false

    private lateinit var recordShortAdapter: RecordShortAdapter

    override fun initAfterBinding() {
        initAdapter()

        //삭제된 record 의 position 을 Observe 하고 있는 라이브 데이터
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("removedPosition")?.observe(viewLifecycleOwner) {position ->
            recordShortAdapter.removeItem(position, 1)
        }

        observe()
    }

    private fun initAdapter() {
        recordShortAdapter = RecordShortAdapter(mutableListOf(Record(0, null), Record(2, null)))
        recordShortAdapter.setMyClickListener(object : RecordShortAdapter.MyClickListener {
            override fun onClick(record: Record, position: Int) {
                /*val action = BySenseFragmentDirections.actionGlobalRecordDialogFragment(record, position)
                findNavController().navigate(action)*/
            }

            override fun changeFilter(filter: String) {
                recordShortAdapter.clearData()
                clearPaging()   //페이징 관련 데이터 초기화
                getPosts(page, filter)  //선택된 감각으로 기록 조회
                feedVm.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), filter, null, null)  //선택된 감각의 총 기록 개수 조회
            }
        })
        binding.bySenseRv.adapter = recordShortAdapter

        getPosts(0, recordShortAdapter.getSenseFilter())    //시각 기록 조회
        feedVm.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), recordShortAdapter.getSenseFilter(), null, null) //시각 기록 총 개수 조회
    }

    private fun getPosts(page: Int, category: String) {
        feedVm.getPosts(SpfUtils.getIntEncryptedSpf("userId"), page, "id,desc", null, null, category)
    }

    private fun clearPaging() {
        page = 0
        isLast = false
    }

    private fun observe() {
        //기록 조회 API 호출 여부를 판단하기 위한 LiveData Observing
        mainVm.callGetPostsFlag.observe(viewLifecycleOwner, Observer {
            val callGetPostsFlag = it.getContentIfNotHandled()

            if (callGetPostsFlag!=null && callGetPostsFlag) {
                clearPaging()   //페이징 관련 데이터 초기화
                getPosts(page, recordShortAdapter.getSenseFilter())    //현재 선택돼 있는 감각 필터에 대한 기록 조회
                feedVm.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), recordShortAdapter.getSenseFilter(), null, null) //현재 선택돼 있는 감각 필터에 대한 총 기록 개수 조회
            }
        })

        feedVm.posts.observe(viewLifecycleOwner, Observer {
            val posts = it.getContentIfNotHandled()
            Log.d("BySenseFragment", "posts Observe!! -> $posts")

            if (posts!=null) {
                clearPaging()   //페이징 관련 데이터 초기화
                recordShortAdapter.addData(posts.content)
            }
        })

        feedVm.feedCnt.observe(viewLifecycleOwner, Observer {
            recordShortAdapter.setCnt(it)
        })
    }
}