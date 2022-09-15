package com.mangpo.taste.view

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
            getBooleanSpf("onBoarding", false)==false -> {
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(5000L)
                    startActivityWithClear(OnBoardingActivity::class.java)
                }
            }
            getStrEncryptedSpf("jwt")==null -> {
                startActivityWithClear(LoginActivity::class.java)
            }
            else -> {
                startActivityWithClear(MainActivity::class.java)
            }
        }
    }
}