package com.mangpo.taste.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentBadgeInfoBottomSheetBinding
import com.mangpo.taste.util.BottomSheetDialogUtils

class BadgeInfoBottomSheetFragment : BottomSheetDialogFragment() {
    interface EventListener {
        fun goRecord()
    }

    private lateinit var binding: FragmentBadgeInfoBottomSheetBinding
    private lateinit var eventListener: EventListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        //높이 설정
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            BottomSheetDialogUtils.setupRatio(requireContext(), bottomSheetDialog, 0.46)
        }

        return dialog
    }

    //다이얼로그 위 테두리가 둥글게 돼 있는 테마로 설정
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBadgeInfoBottomSheetBinding.inflate(inflater, container, false)
        binding.apply {
            fragment = this@BadgeInfoBottomSheetFragment
            badge = arguments?.getParcelable("badge")
        }

        return binding.root
    }

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    fun clickedBtnAfter(seqNum: Int) {
        dismiss()

        if (seqNum==3) {

        } else {
            eventListener.goRecord()
        }
    }
}