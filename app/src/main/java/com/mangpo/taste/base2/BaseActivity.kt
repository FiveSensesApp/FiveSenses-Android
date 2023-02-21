package com.mangpo.taste.base2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.OnBoardingActivity
import com.mangpo.taste.view.OneBtnDialogFragment
import com.mangpo.taste.view.model.OneBtnDialog
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil.hideKeyboard

abstract class BaseActivity<T: ViewDataBinding>(private val layoutResId: Int): AppCompatActivity() {
    protected lateinit var binding: T
    private lateinit var imm: InputMethodManager
    private lateinit var refreshTokenErrorDialog: OneBtnDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        initAfterBinding()
    }

    protected abstract fun initAfterBinding()

    private fun initRefreshTokenErrorDialog() {
        refreshTokenErrorDialog = OneBtnDialogFragment()
        refreshTokenErrorDialog.setMyCallback(object : OneBtnDialogFragment.MyCallback {
            override fun end() {
                SpfUtils.clear()
                SpfUtils.writeSpf("onBoarding", true)

                val intent: Intent = Intent(this@BaseActivity, OnBoardingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("currentItem", 3)
                startActivity(intent)
            }
        })

        val oneBtnDialog: OneBtnDialog = OneBtnDialog("재로그인이 필요합니다.", "토큰에 문제가 발생해 재로그인이 필요합니다.\n로그인 화면으로 이동합니다.", "확인", listOf(46, 10, 46, 12))
        val bundle: Bundle = Bundle()
        bundle.putParcelable("data", oneBtnDialog)
        refreshTokenErrorDialog.arguments = bundle
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun startNextActivity(activity: Class<*>?) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    fun startActivityWithClear(activity: Class<*>?) {
        val intent = Intent(this, activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    // 키보드 보이기
    fun showKeyboard(v: View){
        imm?.showSoftInput(v, 0)
    }

    // 키보드 숨기기
    /*fun hideKeyboard(v: View){
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }*/

    //상태바 텍스트 보여주기
    fun showStatusBarText() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

        }
    }

    fun setCommonObserver(vmList: List<BaseViewModel>) {
        vmList.forEach { vm ->
            vm.toast.observe(this, Observer {
                val msg = it.getContentIfNotHandled()

                if (msg!=null) {
//                    hideKeyboard(binding.root)
                    hideKeyboard(this)
                    showToast(msg)
                }
            })

            vm.tokenExpired.observe(this, Observer {
                if (it && !refreshTokenErrorDialog.isAdded) {
                    refreshTokenErrorDialog.show(supportFragmentManager, null)
                }
            })
        }

        initRefreshTokenErrorDialog()
    }
}