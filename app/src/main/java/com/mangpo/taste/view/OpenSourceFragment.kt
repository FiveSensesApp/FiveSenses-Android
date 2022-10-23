package com.mangpo.taste.view

import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentOpenSourceBinding
import com.mangpo.taste.util.readTxtFile

class OpenSourceFragment : BaseFragment<FragmentOpenSourceBinding>(FragmentOpenSourceBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()

        val text = readTxtFile(requireContext(), R.raw.opensource_license)
        if (text!=null)
            binding.openSourceContentTv.text = text
    }

    private fun setMyEventListener() {
        binding.openSourceBackIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}