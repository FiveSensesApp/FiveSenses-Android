package com.mangpo.taste.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentEventDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils

class EventDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentEventDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_dialog, container, false)
        binding.apply {
            fragment = this@EventDialogFragment
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //넓이 0.83%
        DialogFragmentUtils.resizeWidth(
            requireContext(),
            this@EventDialogFragment,
            0.89f
        )
    }

    fun goUpdateEventActivity() {
        startActivity(Intent(requireContext(), UpdateEventActivity::class.java))
        dismiss()
    }
}