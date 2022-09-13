package com.mangpo.taste.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentCheckPoliciesBottomSheetBinding
import com.mangpo.taste.util.BottomSheetDialogUtils

class CheckPoliciesBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCheckPoliciesBottomSheetBinding

    var allIbCheckState: Boolean = false
    var tcIbCheckState: Boolean = false
    var ppIbCheckState: Boolean = false
    var miIbCheckState: Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        //높이 설정
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            BottomSheetDialogUtils.setupRatio(requireContext(), bottomSheetDialog, 0.75)
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckPoliciesBottomSheetBinding.inflate(inflater, container, false)

        binding.apply {
            fragment = this@CheckPoliciesBottomSheetFragment
        }

        return binding.root
    }

    //다이얼로그 위 테두리가 둥글게 돼 있는 테마로 설정
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    fun check(type: String) {
        when (type) {
            "all" -> {
                allIbCheckState = !allIbCheckState
                tcIbCheckState = !tcIbCheckState
                ppIbCheckState = !ppIbCheckState
                miIbCheckState = !miIbCheckState
            }
            "tc" -> tcIbCheckState = !tcIbCheckState
            "pp" -> ppIbCheckState = !ppIbCheckState
            "mi" -> miIbCheckState = !miIbCheckState
        }

        binding.invalidateAll()
    }
}