package com.mangpo.taste.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.mangpo.taste.databinding.FragmentSelectAgainDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils

class SelectAgainDialogFragment : DialogFragment() {
    interface MyClickCallback {
        fun keep()
        fun back()
    }

    private lateinit var binding: FragmentSelectAgainDialogBinding
    private lateinit var myClickCallback: MyClickCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectAgainDialogBinding.inflate(inflater, container, false)

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
            this@SelectAgainDialogFragment,
            0.86f
        )
    }

    private fun setMyEventListener() {
        binding.selectAgainKeepWritingBtn.setOnClickListener {
            dismiss()
            myClickCallback.keep()
        }

        binding.selectAgainGoBackBtn.setOnClickListener {
            dismiss()
            myClickCallback.back()
        }
    }

    fun setMyClickCallback(myClickCallback: MyClickCallback) {
        this.myClickCallback = myClickCallback
    }
}