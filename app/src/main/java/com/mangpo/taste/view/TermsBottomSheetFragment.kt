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
import com.mangpo.taste.util.readTxtFile

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

        when (args.type) {
            0 -> {
                binding.termsTermsTitleTv.text = getString(R.string.title_ogam_terms)
                setText(R.raw.terms_and_conditions)
            }
            1 -> {
                binding.termsTermsTitleTv.text = getString(R.string.title_private_policy_terms)
                setText(R.raw.privacy_policy)
            }
            2 -> {
                binding.termsTermsTitleTv.text = getString(R.string.title_private_policy_terms)
                setText(R.raw.receive_marketing_information)
            }
        }

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

    private fun setText(raw: Int) {
        val text = readTxtFile(requireContext(), raw)
        if (text!=null)
            binding.termsTermsContentTv.text = text
    }
}