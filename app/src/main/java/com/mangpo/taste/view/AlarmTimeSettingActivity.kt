package com.mangpo.taste.view

import android.os.Bundle
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityAlarmTimeSettingBinding

class AlarmTimeSettingActivity : BaseActivity<ActivityAlarmTimeSettingBinding>(ActivityAlarmTimeSettingBinding::inflate) {
    private lateinit var alarmTimeDialogFragment: AlarmTimeDialogFragment

    override fun initAfterBinding() {
        setAlarmTimeDialogFragment()
        setEventLister()
    }

    private fun setEventLister() {
        binding.alarmTimeSettingNextBtn.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putString("time", "오후 10:00")

            alarmTimeDialogFragment.arguments = bundle
            alarmTimeDialogFragment.show(supportFragmentManager, null)
        }
    }

    private fun setAlarmTimeDialogFragment() {
        alarmTimeDialogFragment = AlarmTimeDialogFragment()

        alarmTimeDialogFragment.setCallback(object : AlarmTimeDialogFragment.Callback {
            override fun cancel() {
            }

            override fun complete(time: String) {
                startNextActivity(StartActivity::class.java)
            }
        })
    }
}