package com.mangpo.taste.view

import android.Manifest
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.gun0912.tedpermission.coroutine.TedPermission
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityAlarmTimeSettingBinding
import kotlinx.coroutines.launch

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
            checkNotificationPermission()   //알람 권한 체크(Android 13 Version 이상)
            /*val bundle: Bundle = Bundle()
            bundle.putString("time", "오후 10:00")

            alarmTimeDialogFragment.arguments = bundle
            alarmTimeDialogFragment.show(supportFragmentManager, null)*/
        }
    }

    private fun checkNotificationPermission() {
        lifecycleScope.launch {
            TedPermission.create()
                .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
                .check()

            val intent = Intent(this@AlarmTimeSettingActivity, StartActivity::class.java)
            intent.putExtra("newUser", createUserReqEntity)
            startActivity(intent)
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