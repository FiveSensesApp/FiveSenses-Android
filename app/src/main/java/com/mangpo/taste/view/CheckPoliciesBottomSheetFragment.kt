package com.mangpo.taste.view

import android.app.Dialog
import android.content.Intent
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
    interface Listener {
        fun finish(allChecked: Boolean, agree: Boolean, miIbCheckState: Boolean)
    }

    private lateinit var binding: FragmentCheckPoliciesBottomSheetBinding
    private lateinit var listener: Listener

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

        if (arguments?.getBoolean("isAllChecked", false)!!) {
            allIbCheckState = true
            tcIbCheckState = true
            ppIbCheckState = true
            miIbCheckState = true
        } else {
            if (arguments?.getBoolean("isAgree", false)!!) {
                allIbCheckState = false
                tcIbCheckState = true
                ppIbCheckState = true
                miIbCheckState = false
            } else {
                allIbCheckState = false
                tcIbCheckState = false
                ppIbCheckState = false
                miIbCheckState = false
            }
        }
        binding.invalidateAll()

        return binding.root
    }

    //다이얼로그 위 테두리가 둥글게 돼 있는 테마로 설정
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    fun setMyListener(listener: Listener) {
        this.listener = listener
    }

    fun check(checkIbId: Int) {
        when (checkIbId) {
            binding.checkPoliciesAllAgreeIb.id -> {
                allIbCheckState = !allIbCheckState
                tcIbCheckState = allIbCheckState
                ppIbCheckState = allIbCheckState
                miIbCheckState = allIbCheckState
            }
            binding.checkPoliciesTcIb.id -> tcIbCheckState = !tcIbCheckState
            binding.checkPoliciesPpIb.id -> ppIbCheckState = !ppIbCheckState
            binding.checkPoliciesMiIb.id -> miIbCheckState = !miIbCheckState
        }

        allIbCheckState = ppIbCheckState && tcIbCheckState && miIbCheckState

        binding.invalidateAll()
    }

    fun goPolicyActivity(policyIbId: Int) {
        val intent = Intent(requireContext(), PolicyActivity::class.java)

        when (policyIbId) {
            binding.checkPoliciesTcNextIb.id -> {
                intent.putExtra("title", getString(R.string.title_ogam_terms))
            }
            binding.checkPoliciesPpNextIb.id -> {
                intent.putExtra("title", getString(R.string.title_privacy_policy_no_blank))
            }
            binding.checkPoliciesMiNextIb.id -> {
                intent.putExtra("title", getString(R.string.title_receive_marketing_information))
            }
        }

        startActivity(intent)
    }

    fun finish() {
        listener.finish(allIbCheckState, (tcIbCheckState && ppIbCheckState), miIbCheckState)
        dismiss()
    }
}