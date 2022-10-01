package com.mangpo.taste.view

import androidx.navigation.fragment.findNavController
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentOpenSourceBinding

class OpenSourceFragment : BaseFragment<FragmentOpenSourceBinding>(FragmentOpenSourceBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        binding.openSourceBackIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}