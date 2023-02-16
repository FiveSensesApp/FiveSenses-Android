package com.mangpo.taste.view

import android.Manifest
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseActivity
import com.mangpo.taste.databinding.ActivityAlarmTimeSettingBinding
import com.mangpo.taste.util.checkPermission

class AlarmTimeSettingActivity : BaseActivity<ActivityAlarmTimeSettingBinding>(R.layout.activity_alarm_time_setting) {
    private lateinit var alarmTimeDialogFragment: AlarmTimeDialogFragment
    private lateinit var createUserReqEntity: CreateUserReqEntity

    override fun initAfterBinding() {
        createUserReqEntity = intent.getParcelableExtra<CreateUserReqEntity>("newUser")!!

        setAlarmTimeDialogFragment()
        setEventLister()
    }

    private fun setEventLister() {
        binding.alarmTimeSettingNextBtn.setOnClickListener {
            checkPermission(lifecycleScope, Manifest.permission.POST_NOTIFICATIONS, "푸시 알림 권한을 거부하셨어요. 나중에도 [설정-알림설정]에서 알림을 변경할 수 있어요!") {
                val intent = Intent(this@AlarmTimeSettingActivity, StartActivity::class.java)
                intent.putExtra("newUser", createUserReqEntity)
                startActivity(intent)
            }
        }
    }

    private fun setAlarmTimeDialogFragment() {
        alarmTimeDialogFragment = AlarmTimeDialogFragment()

        alarmTimeDialogFragment.setCallback(object : AlarmTimeDialogFragment.Callback {
            override fun cancel() {
            }

            override fun complete(time: String) {
                createUserReqEntity.isAlarmOn = true
                createUserReqEntity.alarmDate = time

                val intent = Intent(this@AlarmTimeSettingActivity, StartActivity::class.java)
                intent.putExtra("newUser", createUserReqEntity)
                startActivity(intent)
            }
        })
    }
}