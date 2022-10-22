package com.mangpo.taste.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentBadgeInfoBottomSheetBinding
import com.mangpo.taste.util.BottomSheetDialogUtils
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.viewmodel.BadgeInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadgeInfoBottomSheetFragment : BottomSheetDialogFragment() {
    interface EventListener {
        fun goRecord()
        fun changeRepresentativeBadge(badgeId: String)
    }

    private val vm: BadgeInfoViewModel by viewModels()

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
            vm = this@BadgeInfoBottomSheetFragment.vm
            lifecycleOwner = viewLifecycleOwner
            badge = arguments?.getParcelable("badge")
        }

        observe()

        return binding.root
    }

    private fun observe() {
        vm.toast.observe(viewLifecycleOwner, Observer {
            val toast = it.getContentIfNotHandled()

            if (toast != null) {
                Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
            }
        })

        vm.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                (requireActivity() as BadgeActivity).showLoading()
            } else {
                (requireActivity() as BadgeActivity).hideLoading()
            }
        })

        vm.updateBadgeResult.observe(viewLifecycleOwner, Observer {
            if (it == 200) {
                eventListener.changeRepresentativeBadge(SpfUtils.getStrSpf("badgeRepresent")!!)
                dismiss()
            }
        })
    }

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    fun clickedBtnAfter(seqNum: Int) {
        dismiss()

        if (seqNum == 3) {

        } else {
            eventListener.goRecord()
        }
    }
}