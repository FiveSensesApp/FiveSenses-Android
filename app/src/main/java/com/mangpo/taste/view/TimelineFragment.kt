package com.mangpo.taste.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTimelineBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.checkPermission
import com.mangpo.taste.view.adpater.RecordDetailAdapter
import com.mangpo.taste.view.model.TwoBtnDialog
import com.mangpo.taste.viewmodel.FeedViewModel
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimelineFragment : BaseFragment<FragmentTimelineBinding, FeedViewModel>(FragmentTimelineBinding::inflate) {
    private val mainVm: MainViewModel by activityViewModels()
    override val viewModel: FeedViewModel by viewModels()

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

        initTwoBtnDialog()
        initAdapter()
        observe()
    }

    override fun initAfterBinding() {
    }

    private fun initTwoBtnDialog() {
        twoBtnDialogFragment = TwoBtnDialogFragment()
        twoBtnDialogFragment.setMyCallback(object : TwoBtnDialogFragment.MyCallback {
            override fun leftAction(action: String) { //삭제하기, 이미지 저장
                when (action) {
                    getString(R.string.action_delete_long) -> viewModel.deletePost(recordDetailAdapter.getDeletePostId())
                    getString(R.string.action_save_image) -> {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            checkPermission(lifecycleScope, Manifest.permission.WRITE_EXTERNAL_STORAGE, "이미지 저장을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.") { afterCheckPermission(it, 0) }
                        } else {
                            checkPermission(lifecycleScope, Manifest.permission.READ_MEDIA_IMAGES, "이미지 저장을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.") { afterCheckPermission(it, 0) }
                        }
                    }
                }
            }

            override fun rightAction(action: String) {    //뒤로가기, SNS 공유
                when (action) {
                    getString(R.string.action_go_back) -> {

                    }
                    getString(R.string.action_share_SNS) -> {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            checkPermission(lifecycleScope, Manifest.permission.WRITE_EXTERNAL_STORAGE, "공유하기 기능을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.") { afterCheckPermission(it, 1) }
                        } else {
                            checkPermission(lifecycleScope, Manifest.permission.READ_MEDIA_IMAGES, "공유하기 기능을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.") { afterCheckPermission(it, 1) }
                        }
                    }
                }
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

            override fun share() {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", TwoBtnDialog(getString(R.string.action_sharing), getString(R.string.msg_share_your_preferences), getString(R.string.action_save_image), getString(R.string.action_share_SNS), R.drawable.bg_gy01_12))
                twoBtnDialogFragment.arguments = bundle
                twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
            }

            override fun changeSortFilter(sort: String) {   //정렬 필터(최신순, 오래된순)가 바뀔 때 호출되는 함수
                clearPaging()   //page, isLast 초기화
                viewModel.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, null, null)
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
        viewModel.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, null, null)
        getPosts(page, recordDetailAdapter.getFilter())
    }

    //기록 목록 조회 API 호출
    private fun getPosts(page: Int, sort: String) {
        viewModel.getPosts(SpfUtils.getIntEncryptedSpf("userId"), page, "id,$sort", null, null, null)
    }

    private fun clearPaging() {
        page = 0
        isLast = true
    }

    private fun afterCheckPermission(isGranted: Boolean, action: Int) {
        if (isGranted) {
            goPreviewActivity(action)
        }
    }

    private fun goPreviewActivity(action: Int) {
        val content: ContentEntity = recordDetailAdapter.getContentById(recordDetailAdapter.getSharedPostId())

        val intent: Intent = Intent(requireContext(), PreviewActivity::class.java)
        intent.putExtra("content", content)
        intent.putExtra("action", action)

        startActivity(intent)
    }

    private fun observe() {
        //기록 조회 API 호출 여부를 판단하기 위한 LiveData Observing
        mainVm.callGetPostsFlag.observe(viewLifecycleOwner, Observer {
            val callGetPostsFlag = it.getContentIfNotHandled()

            if (callGetPostsFlag!=null && callGetPostsFlag) {
                clearPaging()   //페이징 관련 데이터 초기화
                recordDetailAdapter.clearData() //현재 리사이클러뷰에 있는 content 데이터들 지우기

                viewModel.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, null, null)    //전체 개수 조회 API 호출
                getPosts(page, recordDetailAdapter.getFilter()) //기록 조회 API 호출
            }
        })

        viewModel.posts.observe(viewLifecycleOwner, Observer {
            val posts = it.getContentIfNotHandled()

            if (posts!=null) {
                page = posts.pageNumber
                isLast = posts.isLast
                recordDetailAdapter.addData(posts.content)
            }
        })

        viewModel.deletePostResult.observe(viewLifecycleOwner, Observer {
            when (it.getContentIfNotHandled()) {
                200 -> recordDetailAdapter.removeData()
                404 -> showToast("삭제 중 문제가 발생했습니다.")
                else -> {}
            }
        })

        viewModel.feedCnt.observe(viewLifecycleOwner, Observer {
            val feedCnt = it.getContentIfNotHandled()

            if (feedCnt!=null) {
                recordDetailAdapter.setCnt(feedCnt)
            }
        })
    }
}