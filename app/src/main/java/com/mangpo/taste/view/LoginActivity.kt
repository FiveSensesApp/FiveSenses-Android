package com.mangpo.taste.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityLoginBinding
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
//    private val mainViewModel: MainViewModel by viewModels()

    override fun initAfterBinding() {
        binding.loginTv.setOnClickListener {
//            mainViewModel.getBooksByName("R")

            startNextActivity(MainActivity::class.java)
        }
    }

    private fun observe() {
        /*mainViewModel.toast.observe(this@LoginActivity) {
            it.getContentIfNotHandled()?.let { value ->
                showToast(value)
            }
        }

        mainViewModel.isLoading.observe(this@LoginActivity) {
            if (it)
                Log.d("MainActivity", "isLoading Observe!! -> 로딩중")
            else
                Log.d("MainActivity", "isLoading Observe!! -> 로딩끝")
        }

        mainViewModel.books.observe(this@LoginActivity) {
            Log.d("MainActivity", "books Observe!! -> $it")
        }*/
    }
}