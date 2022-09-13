package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityAlarmTimeSettingBinding

class AlarmTimeSettingActivity : BaseActivity<ActivityAlarmTimeSettingBinding>(ActivityAlarmTimeSettingBinding::inflate) {
    private lateinit var alarmTimeDialogFragment: AlarmTimeDialogFragment
    private lateinit var createUserReqEntity: CreateUserReqEntity

    override fun initAfterBinding() {
        createUserReqEntity = intent.getParcelableExtra<CreateUserReqEntity>("newUser")!!

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
                createUserReqEntity.alarmDate = time

                val intent = Intent(this@AlarmTimeSettingActivity, StartActivity::class.java)
                intent.putExtra("newUser", createUserReqEntity)
                startActivity(intent)
            }
        })
    }
}