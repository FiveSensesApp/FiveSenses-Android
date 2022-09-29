package com.mangpo.taste.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTimelineBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.adpater.RecordDetailAdapter
import com.mangpo.taste.view.model.TwoBtnDialog
import com.mangpo.taste.viewmodel.FeedViewModel
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimelineFragment : BaseFragment<FragmentTimelineBinding>(FragmentTimelineBinding::inflate) {
    private val mainVm: MainViewModel by activityViewModels()
    private val feedVm: FeedViewModel by viewModels()

    private var page: Int = 0
    private var isLast: Boolean = true

    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var recordDetailAdapter: RecordDetailAdapter
    private lateinit var updateCompleteLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent = result.data!!
                val updatedPost: UpdatePostResEntity = data.getParcelableExtra<UpdatePostResEntity>("updatedPost")!!
                recordDetailAdapter.updateData(updatedPost)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TimelineFragment", "onViewCreated")
        initTwoBtnDialog()
        initAdapter()
        observe()
    }

    override fun initAfterBinding() {
    }

    private fun initTwoBtnDialog() {
        twoBtnDialogFragment = TwoBtnDialogFragment()
        twoBtnDialogFragment.setMyCallback(object : TwoBtnDialogFragment.MyCallback {
            override fun leftAction() {
                feedVm.deletePost(recordDetailAdapter.getDeletePostId())
            }

            override fun rightAction() {
            }
        })
    }

    private fun initAdapter() {
        recordDetailAdapter = RecordDetailAdapter()

        recordDetailAdapter.setMyClickListener(object : RecordDetailAdapter.MyClickListener {
            override fun update(content: ContentEntity) {
                val intent = Intent(requireContext(), RecordUpdateActivity::class.java)
                intent.putExtra("content", content)
                updateCompleteLauncher.launch(intent)
            }

            override fun delete() {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", TwoBtnDialog(getString(R.string.msg_really_delete), getString(R.string.msg_cannot_recover), getString(R.string.action_delete_long), getString(R.string.action_go_back), R.drawable.bg_gy01_12))
                twoBtnDialogFragment.arguments = bundle
                twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
            }

            override fun changeSortFilter(sort: String) {   //정렬 필터(최신순, 오래된순)가 바뀔 때 호출되는 함수
                clearPaging()   //page, isLast 초기화
                getPosts(page, sort)    //정렬 필터에 따라 getPosts API 호출
            }

            override fun callGetPostsAPI(filter: String) {
                clearPaging()
                getPosts(page, filter)
            }
        })

        //무한스크롤 구현
        binding.timelineRecordRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //스크롤이 최하단에 있을 때
                if (!binding.timelineRecordRv.canScrollVertically(1)) {
                    if (!isLast) {  //마지막 페이지가 아니면 현재페이지+1, 현재 선택돼 있는 정렬 형태를 가져다 getPosts API 호출
                        getPosts(page+1, recordDetailAdapter.getFilter())
                    }
                }
            }
        })

        binding.timelineRecordRv.adapter = recordDetailAdapter

        getPosts(page, recordDetailAdapter.getFilter())
    }

    //기록 목록 조회 API 호출
    private fun getPosts(page: Int, sort: String) {
        feedVm.getPosts(SpfUtils.getIntEncryptedSpf("userId"), page, "id,$sort", null, null, null)
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
                recordDetailAdapter.clearData() //현재 리사이클러뷰에 있는 content 데이터들 지우기
                getPosts(page, recordDetailAdapter.getFilter())
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
                recordDetailAdapter.addData(posts.content)
            }
        })

        feedVm.deletePostResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                200 -> recordDetailAdapter.removeData()
                404 -> showToast("삭제 중 문제가 발생했습니다.")
                else -> {}
            }
        })
    }
}