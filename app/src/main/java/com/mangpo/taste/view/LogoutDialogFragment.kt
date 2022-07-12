package com.mangpo.taste.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentLogoutDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils

class LogoutDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentLogoutDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutDialogBinding.inflate(inflater, container, false)

        //모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setMyEventListener()

        return binding.root
    }

    private fun setMyEventListener() {
        //예 버튼 클릭 리스너
        binding.logoutYesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_logoutDialogFragment_to_loginActivity)
        }
    }

    override fun onResume() {
        super.onResume()

        //넓이 0.83%
        DialogFragmentUtils.resizeWidth(
            requireContext(),
            this@LogoutDialogFragment,
            0.86f
        )
    }
}