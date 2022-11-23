package com.mangpo.taste.view

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentBadgeInfoBottomSheetBinding
import com.mangpo.taste.util.*
import com.mangpo.taste.viewmodel.BadgeInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class BadgeInfoBottomSheetFragment : BottomSheetDialogFragment() {
    interface EventListener {
        fun goRecord()
        fun goReview()
        fun changeRepresentativeBadge(badgeId: String)
    }

    private val vm: BadgeInfoViewModel by activityViewModels()

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

        observe()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragment = this@BadgeInfoBottomSheetFragment
            vm = this@BadgeInfoBottomSheetFragment.vm
            lifecycleOwner = viewLifecycleOwner
            badge = arguments?.getParcelable("badge")
            today = formatDate(LocalDate.now().toString(), "yyyy.MM.dd")
        }
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
            val updateBadgeResult = it.getContentIfNotHandled()

            if (updateBadgeResult!=null && updateBadgeResult==200) {
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
            eventListener.goReview()
        } else {
            eventListener.goRecord()
        }
    }

    fun saveAsImage() {
        checkPermission(lifecycleScope, Manifest.permission.WRITE_EXTERNAL_STORAGE, "이미지 저장을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.") { afterCheckPermission(it) }
    }

    private fun afterCheckPermission(isGranted: Boolean) {
        if (isGranted) {
            binding.cl.visibility = View.VISIBLE
            val bitmap = getBitmapFromView(binding.cl.measuredWidth, binding.cl.measuredHeight, binding.cl, Color.WHITE)
            saveImage(bitmap!!, requireContext(), getString(R.string.app_name)) { result, uri -> afterSaveImage(result) }
        }
    }

    private fun afterSaveImage(result: Boolean) {
        if (!result) {
            Toast.makeText(requireContext(), "이미지 저장 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.cl.visibility = View.INVISIBLE
        dismiss()
    }
}