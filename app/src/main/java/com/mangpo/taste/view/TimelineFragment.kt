package com.mangpo.taste.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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

    private var isBack: Boolean = false
    private var page: Int = 0
    private var isLast: Boolean = false
    private var selectedPosition: Int = 0   //선택한 기록의 아이템 위치를 기록하는 변수

    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var recordDetailAdapter: RecordDetailAdapter
    private lateinit var updateCompleteLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent = result.data!!
                val updatedPost: UpdatePostResEntity = data.getParcelableExtra<UpdatePostResEntity>("updatedPost")!!
                recordDetailAdapter.updateData(selectedPosition, updatedPost)
            }
        }
    }

    override fun initAfterBinding() {
        if (!isBack) {
            initTwoBtnDialog()
            initAdapter()
            observe()
            getPosts(page, recordDetailAdapter.getFilter())
        }

    }

    override fun onResume() {
        super.onResume()
        binding.timelineRecordRv.scrollToPosition(selectedPosition) //선택한 기록의 아이템 위치로 이동
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
                isBack = true
                selectedPosition = recordDetailAdapter.getPositionByPostId(content.id)

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
                //page, isLast 초기화
                page = 0
                isLast = false

                //정렬 필터에 따라 getPosts API 호출
                getPosts(page, sort)
            }

            override fun callGetPostsAPI(filter: String) {
                page = 0
                isLast = false

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
    }

    //기록 목록 조회 API 호출
    private fun getPosts(page: Int, sort: String) {
        feedVm.getPosts(SpfUtils.getIntEncryptedSpf("userId"), page, "id,$sort", null, null, null)
    }

    private fun observe() {
        //기록 조회 API 호출 여부를 판단하기 위한 LiveData Observing
        mainVm.callGetPostsFlag.observe(viewLifecycleOwner, Observer {
            val callGetPostsFlag = it.getContentIfNotHandled()

            if (callGetPostsFlag!=null && callGetPostsFlag) {
                page = 0
                isLast = false
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
                //set page & isLast
                page = posts.pageNumber
                isLast = posts.isLast

                if (!posts.empty) {    //빈 데이터가 아니면 adapter 에 데이터 추가
                    if (page==0) {  //단 첫번재 페이지면 데이터들 모두 싹 지우고 추가
                        recordDetailAdapter.clearData()
                    }

                    recordDetailAdapter.addData(posts.content)
                } else {    //데이터가 비어있을 때
                    if (page==0) {  //첫번재 페이지면 클리어
                        recordDetailAdapter.clearData()
                    }
                }
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