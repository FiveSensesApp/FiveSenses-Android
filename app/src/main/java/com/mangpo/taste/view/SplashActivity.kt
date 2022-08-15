package com.mangpo.taste.view

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivitySplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showStatusBarText()
    }

    override fun initAfterBinding() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(3000L)
//            startActivityWithClear(LoginActivity::class.java)
            startActivityWithClear(OnBoardingActivity::class.java)
        }
    }
}