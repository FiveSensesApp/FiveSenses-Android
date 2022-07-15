package com.mangpo.taste.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.mangpo.taste.databinding.FragmentRecordCompleteDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils

class RecordCompleteDialogFragment : DialogFragment() {
    interface MyCallback {
        fun keep()
        fun complete()
    }

    private lateinit var binding: FragmentRecordCompleteDialogBinding
    private lateinit var myCallback: MyCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordCompleteDialogBinding.inflate(inflater, container, false)

        dialog?.setCancelable(false)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        setMyEventListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //넓이 0.86%
        DialogFragmentUtils.resizeWidth(
            requireContext(),
            this@RecordCompleteDialogFragment,
            0.86f
        )
    }

    private fun setMyEventListener() {
        //계속 쓰기 버튼 클릭 리스너
        binding.recordCompleteKeepWritingBtn.setOnClickListener {
            dismiss()
            myCallback.keep()
        }

        //보관함 가기 클릭 리스너
        binding.recordCompleteGoLockerBtn.setOnClickListener {
            dismiss()
            myCallback.complete()
        }
    }

    fun setMyCallback(myCallback: MyCallback) {
        this.myCallback = myCallback
    }
}