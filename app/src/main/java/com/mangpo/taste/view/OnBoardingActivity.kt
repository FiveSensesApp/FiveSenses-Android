package com.mangpo.taste.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : FragmentActivity() {
    val vpPosition: MutableLiveData<Int> = MutableLiveData(0)

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var onBoardingVPAdapter: OnBoardingVPAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        binding.apply {
            activity = this@OnBoardingActivity
            lifecycleOwner = this@OnBoardingActivity
        }

        setContentView(binding.root)

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

        initVP()
    }

    private fun initVP() {
        onBoardingVPAdapter = OnBoardingVPAdapter(this)
        binding.onBoardingVp.apply {
            adapter = onBoardingVPAdapter
            isUserInputEnabled = false
        }
    }

    fun nextPage() {
        binding.onBoardingVp.currentItem++
        vpPosition.postValue(binding.onBoardingVp.currentItem)
    }

    fun goLauncherActivity() {
        startActivity(Intent(this, LauncherActivity::class.java))
    }

    inner class OnBoardingVPAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
        private val fragments = listOf<Fragment>(OnBoardingFragment(R.drawable.on_boarding1), OnBoardingFragment(R.drawable.on_boarding2), OnBoardingFragment(R.drawable.on_boarding3), OnBoardingFragment(R.drawable.on_boarding4))

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}