package com.mangpo.taste.view

import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseActivity
import com.mangpo.taste.databinding.ActivityOnBoardingBinding
import com.mangpo.taste.util.SpfUtils.writeSpf

class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {
    val vpPosition: MutableLiveData<Int> = MutableLiveData<Int>(0)
    private lateinit var onBoardingVPAdapter: OnBoardingVPAdapter

    override fun initAfterBinding() {
        binding.apply {
            this.activity = this@OnBoardingActivity
        }

        //상태바 설정
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

        initVP(intent.getIntExtra("currentItem", 0))
    }

    private fun initVP(currentItem: Int) {
        onBoardingVPAdapter = OnBoardingVPAdapter(this)

        binding.onBoardingPiv.count = onBoardingVPAdapter.itemCount

        binding.onBoardingVp.adapter = onBoardingVPAdapter
        binding.onBoardingVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.onBoardingPiv.setSelected(position)
                vpPosition.postValue(position)
            }
        })
        binding.onBoardingVp.doOnAttach  {
            binding.onBoardingVp.setCurrentItem(currentItem, false)
        }
    }

    fun goLoginActivity(view: TextView) {
        writeSpf("onBoarding", true)
        startActivityWithClear(LoginActivity::class.java)
    }

    fun onClickBtn(view: AppCompatButton) {
        if (view.text.toString()==getString(R.string.action_next)) {
            binding.onBoardingVp.currentItem++
        } else {
            startNextActivity(CreateAccountActivity::class.java)
        }
    }

    inner class OnBoardingVPAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
        private val fragments = listOf<Fragment>(OnBoardingFragment(getString(R.string.msg_on_boarding1), R.drawable.ic_on_boarding1), OnBoardingFragment(getString(R.string.msg_on_boarding2), R.drawable.ic_on_boarding2), OnBoardingFragment(getString(R.string.msg_on_boarding3), R.drawable.ic_on_boarding3), OnBoardingFragment(getString(R.string.msg_on_boarding4), R.drawable.ic_on_boarding4))

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}