package com.mangpo.taste.view

import androidx.navigation.fragment.findNavController
import com.mangpo.taste.base.BaseNoVMFragment
import com.mangpo.taste.databinding.FragmentSearchResultBinding

class SearchResultFragment : BaseNoVMFragment<FragmentSearchResultBinding>(FragmentSearchResultBinding::inflate) {
    override fun initAfterBinding() {
        //삭제된 record 의 position 을 Observe 하고 있는 라이브 데이터
        findNavController().previousBackStackEntry?.savedStateHandle?.getLiveData<String>("search")?.observe(viewLifecycleOwner) {search ->
        }
    }
}