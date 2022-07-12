package com.mangpo.taste.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentPwResettingDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils

class PwResettingDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentPwResettingDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPwResettingDialogBinding.inflate(inflater, container, false)

        //모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setMyEventListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //넓이 0.86%
        DialogFragmentUtils.resizeWidth(
            requireContext(),
            this@PwResettingDialogFragment,
            0.86f
        )
    }

    private fun setMyEventListener() {
        //또 만나요! 버튼 클릭 리스너
        binding.pwResettingDialogSeeYouAgainBtn.setOnClickListener {
            findNavController().navigate(R.id.action_pwResettingDialogFragment_to_loginActivity)
        }
    }
}