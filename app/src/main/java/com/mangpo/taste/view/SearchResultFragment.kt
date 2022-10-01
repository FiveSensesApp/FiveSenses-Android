package com.mangpo.taste.view

import androidx.navigation.fragment.findNavController
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentSearchResultBinding

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(FragmentSearchResultBinding::inflate) {
//    private val args: SearchResultFragmentArgs by navArgs()

    override fun initAfterBinding() {
        //삭제된 record 의 position 을 Observe 하고 있는 라이브 데이터
        findNavController().previousBackStackEntry?.savedStateHandle?.getLiveData<String>("search")?.observe(viewLifecycleOwner) {search ->
        }
    }
}