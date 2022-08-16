package com.mangpo.taste.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.mangpo.taste.databinding.FragmentAlarmTimeDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils

class AlarmTimeDialogFragment() : DialogFragment() {
    interface Callback {
        fun cancel()
        fun complete(time: String)
    }

    private val timeUnitList = listOf<String>("오전", "오후")
    private val hourList = (1..12).toList().map { it.toString().padStart(2, '0') }
    private val minuteList = (0..60).toList().map { it.toString().padStart(2, '0') }

    private lateinit var binding: FragmentAlarmTimeDialogBinding
    private lateinit var callback: Callback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmTimeDialogBinding.inflate(inflater, container, false)

        //모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        initNumberPicker(arguments?.getString("time")!!)
        setEventListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //넓이 0.83%
        DialogFragmentUtils.resizeWidth(
            requireContext(),
            this@AlarmTimeDialogFragment,
            0.86f
        )
    }

    //NumberPicker 초기화
    private fun initNumberPicker(time: String) {
        binding.alarmTimeAmPmNp.displayedValues = timeUnitList.toTypedArray()
        binding.alarmTimeAmPmNp.wrapSelectorWheel = true
        binding.alarmTimeAmPmNp.minValue = 0
        binding.alarmTimeAmPmNp.maxValue = timeUnitList.size - 1
        binding.alarmTimeAmPmNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.alarmTimeAmPmNp.value = timeUnitList.indexOf(time.split(" ")[0])

        binding.alarmTimeHourNp.displayedValues = hourList.toTypedArray()
        binding.alarmTimeHourNp.wrapSelectorWheel = true
        binding.alarmTimeHourNp.minValue = 0
        binding.alarmTimeHourNp.maxValue = hourList.size - 1
        binding.alarmTimeHourNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.alarmTimeHourNp.value = hourList.indexOf(time.split(" ")[1].split(":")[0])

        binding.alarmTimeMinuteNp.displayedValues = minuteList.toTypedArray()
        binding.alarmTimeMinuteNp.minValue = 0
        binding.alarmTimeMinuteNp.maxValue = minuteList.size - 1
        binding.alarmTimeMinuteNp.wrapSelectorWheel = true
        binding.alarmTimeMinuteNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.alarmTimeMinuteNp.value = minuteList.indexOf(time.split(" ")[1].split(":")[1])
    }

    private fun setEventListener() {
        binding.alarmTimeCancelBtn.setOnClickListener {
            callback.cancel()
            dismiss()
        }

        binding.alarmTimeCompleteBtn.setOnClickListener {
            callback.complete("${timeUnitList[binding.alarmTimeAmPmNp.value]} ${hourList[binding.alarmTimeHourNp.value]}:${minuteList[binding.alarmTimeMinuteNp.value]}")
            dismiss()
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }
}