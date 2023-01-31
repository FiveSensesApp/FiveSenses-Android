package com.mangpo.taste.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentSearchResultBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.checkPermission
import com.mangpo.taste.util.setSpannableText
import com.mangpo.taste.view.adpater.RecordDetailAdapter
import com.mangpo.taste.view.model.TwoBtnDialog
import com.mangpo.taste.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : BaseFragment<FragmentSearchResultBinding, FeedViewModel>(FragmentSearchResultBinding::inflate) {
    override val viewModel: FeedViewModel by viewModels()

    private val navArgs: SearchResultFragmentArgs by navArgs()

    private var keyword: String = ""

    private lateinit var recordDetailAdapter: RecordDetailAdapter
    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var updateCompleteLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        keyword = navArgs.keyword

        updateCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent = result.data!!
                val updatedPost: UpdatePostResEntity = data.getParcelableExtra<UpdatePostResEntity>("updatedPost")!!

                if (updatedPost.keyword.contains(keyword) || (updatedPost.content?: "").contains(keyword)) {
                    recordDetailAdapter.updateData(updatedPost)
                } else {
                    recordDetailAdapter.removeData(updatedPost.id)
                    setCntText(recordDetailAdapter.itemCount)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragment = this@SearchResultFragment
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
            keyword = this@SearchResultFragment.keyword
        }

        initAdapter()
        initTwoBtnDialog()
        setMyEventListener()
        observe()

        search(this@SearchResultFragment.keyword)
    }

    override fun initAfterBinding() {
    }

    private fun initAdapter() {
        recordDetailAdapter = RecordDetailAdapter(mutableListOf())

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

            override fun changeSortFilter(sort: String) {
            }

            override fun callGetPostsAPI(filter: String) {
            }
        })

        binding.searchResultRv.adapter = recordDetailAdapter
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

    private fun setMyEventListener() {
        binding.searchResultSearchEt.setOnKeyListener { view, keyCode, keyEvent ->
            if(keyCode== KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP){
                keyword = (view as EditText).text.toString()

                val recentSearchTerms = Gson().fromJson<ArrayList<String>>(SpfUtils.getStrSpf("recentSearchTerms"), ArrayList::class.java)?: arrayListOf<String>()
                recentSearchTerms.apply {
                    if (size >= 10) {
                        removeAt(lastIndex)
                    }
                    add(0, keyword)
                    SpfUtils.writeSpf("recentSearchTerms", this.toString())
                }

                (requireActivity() as MainActivity).hideKeyboard(view)

                showToast("KEYBOARD")
                search(keyword)
            }

            false
        }
    }

    private fun search(keyword: String) {
        viewModel.searchKeywordLike(keyword)
    }

    private fun setCntText(size: Int) {
        val pointText = "${size}개의 결과"
        val text = "총 ${pointText}가 존재합니다."
        setSpannableText(text, requireContext(), R.color.BK, text.indexOf(pointText), text.indexOf(pointText)+pointText.length, binding.searchResultCntTv)
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
        viewModel.searchResultPosts.observe(viewLifecycleOwner, Observer {
            setCntText(it.size)

            if (it.isNotEmpty()) {
                recordDetailAdapter.clearData()
                recordDetailAdapter.addData(it)
            }
        })

        viewModel.deletePostResult.observe(viewLifecycleOwner, Observer {
            when (it.getContentIfNotHandled()) {
                200 -> {
                    recordDetailAdapter.removeData(recordDetailAdapter.getDeletePostId())
                    setCntText(recordDetailAdapter.itemCount)
                }
                404 -> showToast("삭제 중 문제가 발생했습니다.")
                else -> {}
            }
        })
    }

    fun popBackStack() {
        findNavController().popBackStack()
    }

    fun deleteSearchKeyword() {
        binding.searchResultSearchEt.apply {
            text.clear()
            requestFocus()
            (requireActivity() as MainActivity).showKeyboard(this)
        }
    }
}