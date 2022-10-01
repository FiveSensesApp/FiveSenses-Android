package com.mangpo.taste.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentTermsBottomSheetBinding
import com.mangpo.taste.util.BottomSheetDialogUtils

class TermsBottomSheetFragment : BottomSheetDialogFragment() {
    private val args: TermsBottomSheetFragmentArgs by navArgs()
    private lateinit var binding: FragmentTermsBottomSheetBinding

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTermsBottomSheetBinding.inflate(inflater, container, false)

        if (args.type==0)   //서비스 이용약관
            binding.termsTermsTitleTv.text = getString(R.string.title_terms_of_service)
        else    //개인정보 처리 방침
            binding.termsTermsTitleTv.text = getString(R.string.title_privacy_policy)

        setMyEventListener()

        return binding.root
    }

    //다이얼로그 위 테두리가 둥글게 돼 있는 테마로 설정
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    private fun setMyEventListener() {
        //닫기 이미지뷰 클릭 리스너
        binding.termsCloseIv.setOnClickListener {
            dismiss()
        }
    }
}