package com.mangpo.taste.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.mangpo.taste.databinding.FragmentOneBtnDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.view.model.OneBtnDialog

class OneBtnDialogFragment : DialogFragment() {
    interface MyCallback {
        fun end()
    }

    private lateinit var binding: FragmentOneBtnDialogBinding
    private lateinit var myCallback: MyCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneBtnDialogBinding.inflate(inflater, container, false)

        //모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        dialog?.setCancelable(false)    //외부 화면 눌러서 다이얼로그 사라지는거 막기

        bind()  //화면 UI 바인딩

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //넓이 0.86%
        DialogFragmentUtils.resizeWidth(
            requireContext(),
            this@OneBtnDialogFragment,
            0.86f
        )
    }

    private fun bind() {
        val data: OneBtnDialog = arguments?.getParcelable<OneBtnDialog>("data")!!

        binding.oneBtnTitleTv.text = data.title
        binding.oneBtnMsgTv.text = data.msg
        binding.oneBtnActionBtn.text = data.action
        binding.oneBtnActionBtn.setPadding(convertDpToPx(requireContext(), data.actionBtnPadding[0]), convertDpToPx(requireContext(), data.actionBtnPadding[1]), convertDpToPx(requireContext(), data.actionBtnPadding[2]), convertDpToPx(requireContext(), data.actionBtnPadding[3]))

        binding.oneBtnActionBtn.setOnClickListener {
            myCallback.end()
            dismiss()   //다이얼로그 종료
        }
    }

    fun setMyCallback(myCallback: MyCallback) {
        this.myCallback = myCallback
    }
}