package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivitySplashBinding
import com.mangpo.taste.util.SpfUtils.getStrEncryptedSpf
import com.mangpo.taste.util.SpfUtils.getBooleanSpf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showStatusBarText()
    }

    override fun initAfterBinding() {
        when {
            !getBooleanSpf("onBoarding", false) -> goNextActivity(OnBoardingActivity::class.java)
            getStrEncryptedSpf("jwt")==null -> {
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(5000L)

                    val intent: Intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                    intent.putExtra("currentItem", 3)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
            else -> goNextActivity(MainActivity::class.java)
        }
    }

    private fun goNextActivity(activity: Class<*>) {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(5000L)
            startActivityWithClear(activity)
        }
    }
}