package com.mangpo.taste.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.mangpo.taste.util.NetworkManager


abstract class BaseActivity<T: ViewBinding>(private val inflate: (LayoutInflater) -> T): AppCompatActivity(){
    protected lateinit var binding: T
        private set

    private var imm : InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?

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

    // 키보드 숨기기
    fun hideKeyboard(v: View){
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }

    //네트워크 연결 유무 확인
    fun checkNetwork(): Boolean = NetworkManager.checkNetworkState(applicationContext)

    //상태바 숨기기
    fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    //상태바 투명하게
    fun transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
}