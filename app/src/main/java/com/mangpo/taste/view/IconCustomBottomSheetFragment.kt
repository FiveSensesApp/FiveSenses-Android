package com.mangpo.taste.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentIconCustomBottomSheetBinding
import com.mangpo.taste.util.BottomSheetDialogUtils

class IconCustomBottomSheetFragment : BottomSheetDialogFragment() {
    interface CallbackListener {
        fun typeSelected(type: Int)
    }

    private lateinit var binding: FragmentIconCustomBottomSheetBinding
    private lateinit var callbackListener: CallbackListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        //높이 설정
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            BottomSheetDialogUtils.setupRatio(requireContext(), bottomSheetDialog, 0.25)
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIconCustomBottomSheetBinding.inflate(inflater, container, false)
        binding.apply {
            fragment = this@IconCustomBottomSheetFragment
            customType = when (arguments?.getInt("customType")) {
                0 -> binding.iconCustom5gaamCharacterRb.id
                1 -> binding.iconCustomEmojiRb.id
                else -> binding.iconCustomOnlyTextRb.id
            }
        }

        return binding.root
    }

    //다이얼로그 위 테두리가 둥글게 돼 있는 테마로 설정
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    fun setCallbackListener(callbackListener: CallbackListener) {
        this.callbackListener = callbackListener
    }

    fun onSplitTypeChanged(radioGroup: RadioGroup, id: Int) {
        when (id) {
            binding.iconCustom5gaamCharacterRb.id -> callbackListener.typeSelected(0)
            binding.iconCustomEmojiRb.id -> callbackListener.typeSelected(1)
            binding.iconCustomOnlyTextRb.id -> callbackListener.typeSelected(2)
        }

        dismiss()
    }
}