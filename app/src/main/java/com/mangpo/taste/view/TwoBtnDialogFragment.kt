package com.mangpo.taste.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentTwoBtnDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils
import com.mangpo.taste.view.model.TwoBtnDialog

class TwoBtnDialogFragment : DialogFragment() {
    interface MyCallback {
        fun leftAction()
        fun rightAction()
    }

    private lateinit var binding: FragmentTwoBtnDialogBinding
    private lateinit var myCallback: MyCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTwoBtnDialogBinding.inflate(inflater, container, false)

        setEventListener()

        bind(arguments?.getParcelable<TwoBtnDialog>("data")!!)  //데이터 바인딩

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //넓이 0.83%
        DialogFragmentUtils.resizeWidth(
            requireContext(),
            this@TwoBtnDialogFragment,
            0.86f
        )
    }

    private fun setEventListener() {
        binding.twoBtnLeftBtn.setOnClickListener {
            dismiss()
            myCallback.leftAction()
        }

        binding.twoBtnRightBtn.setOnClickListener {
            dismiss()
            myCallback.rightAction()
        }
    }

    private fun bind(data: TwoBtnDialog) {
        if (data.isBlurred) {   //투명 처리 -> 뒷배경 하얀색, 블러뷰 VISIBLE
            dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

            binding.twoBtnBlurredView.visibility = View.VISIBLE
        } else {    //투명 처리 x -> 뒷배경 검정, 전체 background bg_gy01_12, 블러뷰 INVISIBLE
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

            binding.root.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_gy01_12)
            binding.twoBtnBlurredView.visibility = View.INVISIBLE
        }

        binding.twoBtnTitleTv.text = data.title
        binding.twoBtnMsgTv.text = data.msg
        binding.twoBtnLeftBtn.text = data.leftAction
        binding.twoBtnRightBtn.text = data.rightAction
    }

    fun setMyCallback(myCallback: MyCallback) {
        this.myCallback = myCallback
    }
}