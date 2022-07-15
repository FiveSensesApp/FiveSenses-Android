package com.mangpo.taste.view

import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentOgamSelectBinding

class OgamSelectFragment : BaseFragment<FragmentOgamSelectBinding>(FragmentOgamSelectBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        binding.root.setOnClickListener {   //감각 선택(임시) -> TasteRecordFragment 로 이동
            (requireActivity() as MainActivity).changeFABIcon(R.drawable.ic_check_29)   //FAB 아이콘 변경(x -> v)
            findNavController().navigate(R.id.action_ogamSelectFragment_to_tasteRecordFragment)
        }

        //FloatingActionButton 클릭 리스너 -> MainActivity onBackPressed
        binding.ogamSelectFab.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}