package com.mangpo.taste.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
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