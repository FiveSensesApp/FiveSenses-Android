package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mangpo.taste.base.BaseNoVMActivity
import com.mangpo.taste.databinding.ActivitySplashBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.SpfUtils.clear
import com.mangpo.taste.util.SpfUtils.getStrEncryptedSpf
import com.mangpo.taste.util.SpfUtils.getBooleanSpf
import com.mangpo.taste.view.model.OneBtnDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseNoVMActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showStatusBarText()
    }

    override fun initAfterBinding() {
        when {
            !getBooleanSpf("onBoarding", false) -> goNextActivity(OnBoardingActivity::class.java)   //onBoarding 화면이 열렸던 데이터가 없음 -> 최초 실행
            getStrEncryptedSpf("jwt")==null -> {    //accessToken 이 없음 -> 로그인 화면 이동
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(5000L)
                    goLogin()
                }
            }
            getStrEncryptedSpf("refreshToken")==null -> {   //accessToken 은 있는데 refreshToken 이 없음 -> 이전 버전에서 업데이트 된 경우
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(3000L)

                    val oneBtnDialog: OneBtnDialog = OneBtnDialog("재로그인이 필요합니다.", "버전 업데이트로 인한 재로그인이 필요합니다.\n로그인 화면으로 이동합니다.", "확인", listOf(46, 10, 46, 12))
                    val bundle: Bundle = Bundle()
                    bundle.putParcelable("data", oneBtnDialog)

                    val oneBtnDialogFragment: OneBtnDialogFragment = OneBtnDialogFragment()
                    oneBtnDialogFragment.setMyCallback(object : OneBtnDialogFragment.MyCallback {
                        override fun end() {
                            clear()
                            SpfUtils.writeSpf("onBoarding", true)
                            goLogin()
                        }
                    })
                    oneBtnDialogFragment.arguments = bundle
                    oneBtnDialogFragment.show(supportFragmentManager, null)
                }
            }
            else -> goNextActivity(MainActivity::class.java)
        }
    }

    private fun goLogin() {
        val intent: Intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
        intent.putExtra("currentItem", 3)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun goNextActivity(activity: Class<*>) {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(5000L)
            startActivityWithClear(activity)
        }
    }
}