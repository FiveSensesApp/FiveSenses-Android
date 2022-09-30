package com.mangpo.taste.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentByScoreBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.adpater.RecordShortAdapter
import com.mangpo.taste.view.model.Record
import com.mangpo.taste.viewmodel.FeedViewModel
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ByScoreFragment : BaseFragment<FragmentByScoreBinding>(FragmentByScoreBinding::inflate) {
    private val mainVm: MainViewModel by activityViewModels()
    private val feedVm: FeedViewModel by viewModels()

    private var page: Int = 0
    private var isLast: Boolean = true
    private var deletedContentId: Int = -1

    private lateinit var recordShortAdapter: RecordShortAdapter
    private lateinit var recordDialogFragment: RecordDialogFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initRecordDialog()
        observe()
    }

    override fun initAfterBinding() {

    }

    private fun initAdapter() {
        recordShortAdapter = RecordShortAdapter(mutableListOf(Record(1, null), Record(2, null)))

        recordShortAdapter.setMyClickListener(object : RecordShortAdapter.MyClickListener {

            override fun onClick(content: ContentEntity) {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("content", content)
                recordDialogFragment.arguments = bundle
                recordDialogFragment.show(requireActivity().supportFragmentManager, null)
            }

            override fun changeFilter(filter: String) {
            }

            override fun changeFilter(filter: Int) {
                clearPaging()   //페이징 관련 데이터 초기화
                recordShortAdapter.clearData()
                feedVm.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, filter, null)  //선택된 감각의 총 기록 개수 조회
                getPosts(page, recordShortAdapter.getScoreFilter())
            }
        })

        //무한스크롤 구현
        binding.byScoreRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //스크롤이 최하단에 있을 때
                if (!binding.byScoreRv.canScrollVertically(1)) {
                    if (!isLast) {  //마지막 페이지가 아니면 현재페이지+1, 현재 선택돼 있는 정렬 형태를 가져다 getPosts API 호출
                        getPosts(page+1, recordShortAdapter.getScoreFilter())
                    }
                }
            }
        })

        binding.byScoreRv.adapter = recordShortAdapter

        feedVm.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, recordShortAdapter.getScoreFilter(), null) //시각 기록 총 개수 조회
        getPosts(page, recordShortAdapter.getScoreFilter())
    }

    private fun initRecordDialog() {
        recordDialogFragment = RecordDialogFragment()
        recordDialogFragment.setEventListener(object : RecordDialogFragment.EventListener {
            override fun close(contentEntity: ContentEntity) {
                if (contentEntity.star==recordShortAdapter.getScoreFilter())   //점수 이외 다른 데이터가 수정됐으면 아이템 업데이트
                    recordShortAdapter.updateData(contentEntity)
                else    //점수가 수정됐으면 리사이클러뷰에서 아이템 삭제
                    recordShortAdapter.removeData(contentEntity.id)
            }

            override fun deleteComplete(contentId: Int) {
                deletedContentId = contentId
                feedVm.deletePost(contentId)
            }
        })
    }

    private fun getPosts(page: Int, star: Int) {
        feedVm.getPosts(SpfUtils.getIntEncryptedSpf("userId"), page, "id,desc", null, star, null)
    }

    private fun clearPaging() {
        page = 0
        isLast = true
    }

    private fun observe() {
        //기록 조회 API 호출 여부를 판단하기 위한 LiveData Observing
        mainVm.callGetPostsFlag.observe(viewLifecycleOwner, Observer {
            val callGetPostsFlag = it.getContentIfNotHandled()

            if (callGetPostsFlag!=null && callGetPostsFlag) {
                clearPaging()   //페이징 관련 데이터 초기화
                recordShortAdapter.clearData()  //현재 리사이클러뷰에 있는 content 데이터들 지우기

                feedVm.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, recordShortAdapter.getScoreFilter(), null) //현재 선택돼 있는 감각 필터에 대한 총 기록 개수 조회
                getPosts(page, recordShortAdapter.getScoreFilter())
            }
        })

        feedVm.toast.observe(viewLifecycleOwner, Observer {
            val msg: String? = it.getContentIfNotHandled()

            if (msg!=null)
                showToast(msg)
        })

        feedVm.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        })

        feedVm.posts.observe(viewLifecycleOwner, Observer {
            val posts = it.getContentIfNotHandled()

            if (posts!=null) {
                page = posts.pageNumber
                isLast = posts.isLast
                recordShortAdapter.addData(posts.content)
            }
        })

        feedVm.feedCnt.observe(viewLifecycleOwner, Observer {
            val feedCnt = it.getContentIfNotHandled()

            if (feedCnt!=null) {
                recordShortAdapter.setCnt(feedCnt)
            }
        })

        feedVm.deletePostResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                200 -> recordShortAdapter.removeData(deletedContentId)
                404 -> showToast("삭제 중 문제가 발생했습니다.")
                else -> {}
            }
        })
    }
}