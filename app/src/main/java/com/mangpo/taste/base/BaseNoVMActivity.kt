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
import androidx.viewbinding.ViewBinding
import com.mangpo.taste.R
import com.mangpo.taste.util.NetworkManager

abstract class BaseNoVMActivity<T: ViewBinding>(private val inflate: (LayoutInflater) -> T): AppCompatActivity(){
    protected lateinit var binding: T
        private set

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
    }

    protected abstract fun initAfterBinding()

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