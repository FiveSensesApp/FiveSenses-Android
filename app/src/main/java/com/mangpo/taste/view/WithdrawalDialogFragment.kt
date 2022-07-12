package com.mangpo.taste.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentLogoutDialogBinding
import com.mangpo.taste.databinding.FragmentWithdrawalDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils

class WithdrawalDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentWithdrawalDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWithdrawalDialogBinding.inflate(inflater, container, false)

        //모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setMyEventListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //넓이 0.83%
        DialogFragmentUtils.resizeWidth(
            requireContext(),
            this@WithdrawalDialogFragment,
            0.86f
        )
    }

    private fun setMyEventListener() {
        //예 버튼 클릭 리스너
        binding.withdrawalYesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_withdrawalDialogFragment_to_loginActivity)
        }
    }
}