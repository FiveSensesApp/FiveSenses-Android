package com.mangpo.taste.base

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.contains
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.mangpo.taste.R
import com.mangpo.taste.util.NetworkManager
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.OnBoardingActivity
import com.mangpo.taste.view.OneBtnDialogFragment
import com.mangpo.taste.view.model.OneBtnDialog

abstract class BaseActivity<T: ViewBinding, K: BaseViewModel>(private val inflate: (LayoutInflater) -> T): AppCompatActivity(){
    abstract val viewModel: K // 뷰모델

    protected lateinit var binding: T
        private set

    private val refreshTokenErrorDialog: OneBtnDialogFragment = OneBtnDialogFragment()

    private var imm : InputMethodManager? = null

    private lateinit var loading: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflate(layoutInflater)
        setContentView(binding.root)

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        try {
            loading = layoutInflater.inflate(R.layout.view_loading, binding.root as ConstraintLayout, false)
        } catch (e: ClassCastException) {
            loading = layoutInflater.inflate(R.layout.view_loading, binding.root as NestedScrollView, false)
        }


        initAfterBinding()
        initRefreshTokenErrorDialog()
        observe()
    }

    protected abstract fun initAfterBinding()

    private fun initRefreshTokenErrorDialog() {
        refreshTokenErrorDialog.setMyCallback(object : OneBtnDialogFragment.MyCallback {
            override fun end() {
                SpfUtils.clear()
                SpfUtils.writeSpf("onBoarding", true)
                goLogin()
            }
        })

        val oneBtnDialog: OneBtnDialog = OneBtnDialog("재로그인이 필요합니다.", "토큰에 문제가 발생해 재로그인이 필요합니다.\n로그인 화면으로 이동합니다.", "확인", listOf(46, 10, 46, 12))
        val bundle: Bundle = Bundle()
        bundle.putParcelable("data", oneBtnDialog)
        refreshTokenErrorDialog.arguments = bundle
    }

    private fun goLogin() {
        val intent: Intent = Intent(this@BaseActivity, OnBoardingActivity::class.java)
        intent.putExtra("currentItem", 3)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun observe() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        viewModel.toast.observe(this, Observer {
            val msg = it.getContentIfNotHandled()

            if (msg!=null) {
                hideKeyboard(binding.root)
                showToast(msg)
            }
        })

        viewModel.tokenExpired.observe(this, Observer {
            if (it && !refreshTokenErrorDialog.isAdded) {
                refreshTokenErrorDialog.show(supportFragmentManager, null)
            }
        })
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
    fun hideKeyboard(v: View){
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }

    //네트워크 연결 유무 확인
    fun checkNetwork(): Boolean = NetworkManager.checkNetworkState(applicationContext)

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

    fun showLoading() {
        if ((binding.root as ConstraintLayout).contains(loading)) {
            hideLoading()
        }

        (binding.root as ConstraintLayout).addView(loading)
    }

    fun hideLoading() {
        if ((binding.root as ConstraintLayout).contains(loading)) {
            (binding.root as ConstraintLayout).removeView(loading)
        }
    }

    fun goUrlPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}