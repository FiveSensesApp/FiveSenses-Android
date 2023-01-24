package com.mangpo.taste.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentSearchResultBinding
import com.mangpo.taste.view.adpater.RecordShortAdapter
import com.mangpo.taste.view.model.Record
import com.mangpo.taste.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : BaseFragment<FragmentSearchResultBinding, FeedViewModel>(FragmentSearchResultBinding::inflate) {
    override val viewModel: FeedViewModel by viewModels()

    private val navArgs: SearchResultFragmentArgs by navArgs()

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
        search(navArgs.keyword)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("keyword")?.observe(viewLifecycleOwner) {result ->
            search(result)
        }
    }

    private fun initAdapter() {
        recordShortAdapter = RecordShortAdapter(mutableListOf(Record(4, null)))

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
            }

            override fun unmarkedDate() {
            }
        })

        binding.searchResultRv.adapter = recordShortAdapter
    }

    private fun initRecordDialog() {
        recordDialogFragment = RecordDialogFragment()
        recordDialogFragment.setEventListener(object : RecordDialogFragment.EventListener {
            override fun close(contentEntity: ContentEntity) {
                recordShortAdapter.updateData(contentEntity)
            }

            override fun delete(contentId: Int) {
                deletedContentId = contentId
                viewModel.deletePost(contentId)
            }
        })
    }

    private fun search(keyword: String) {
        viewModel.searchKeywordLike(keyword)
    }

    private fun observe() {
        viewModel.searchResultPosts.observe(viewLifecycleOwner, Observer {
            recordShortAdapter.clearData()
            recordShortAdapter.addData(it)
        })

        viewModel.deletePostResult.observe(viewLifecycleOwner, Observer {
            when (it.getContentIfNotHandled()) {
                200 -> recordShortAdapter.removeData(deletedContentId)
                404 -> showToast("삭제 중 문제가 발생했습니다.")
                else -> {}
            }
        })
    }
}