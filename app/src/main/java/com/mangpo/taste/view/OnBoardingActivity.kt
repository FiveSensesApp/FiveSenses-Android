package com.mangpo.taste.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ActivityOnBoardingBinding
import com.mangpo.taste.util.SpfUtils.writeSpf

class OnBoardingActivity : FragmentActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var onBoardingVPAdapter: OnBoardingVPAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
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

        initVP(intent.getIntExtra("currentItem", 0))
        setEventListener()

        val dontKnowEmailTvText = SpannableString(binding.onBoardingLoginTv.text.toString())
        dontKnowEmailTvText.setSpan(UnderlineSpan(), 11, 14, 0)
        binding.onBoardingLoginTv.text = dontKnowEmailTvText
    }

    private fun initVP(currentItem: Int) {
        onBoardingVPAdapter = OnBoardingVPAdapter(this)
        binding.onBoardingPiv.count = onBoardingVPAdapter.itemCount
        binding.onBoardingVp.adapter = onBoardingVPAdapter
        binding.onBoardingVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.onBoardingPiv.setSelected(position)

                if (position==3) {
                    binding.onBoardingBtn.text = getString(R.string.title_sign_up)
                    binding.onBoardingLoginTv.visibility = View.VISIBLE
                } else {
                    binding.onBoardingBtn.text = getString(R.string.action_next)
                    binding.onBoardingLoginTv.visibility = View.INVISIBLE
                }
            }
        })
        binding.onBoardingVp.doOnAttach  {
            binding.onBoardingVp.setCurrentItem(currentItem, false)
        }
    }

    private fun setEventListener() {
        binding.onBoardingBtn.setOnClickListener {
            if ((it as AppCompatButton).text.toString()==getString(R.string.action_next)) {
                binding.onBoardingVp.currentItem++
            } else {
                goNextActivity(CreateAccountActivity::class.java)
            }
        }

        binding.onBoardingLoginTv.setOnClickListener {
            goNextActivity(LoginActivity::class.java)
        }
    }

    private fun goNextActivity(activity: Class<*>) {
        writeSpf("onBoarding", true)

        val intent: Intent = Intent(this@OnBoardingActivity, activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    inner class OnBoardingVPAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
        private val fragments = listOf<Fragment>(OnBoardingFragment(getString(R.string.msg_on_boarding1), R.drawable.ic_on_boarding1), OnBoardingFragment(getString(R.string.msg_on_boarding2), R.drawable.ic_on_boarding2), OnBoardingFragment(getString(R.string.msg_on_boarding3), R.drawable.ic_on_boarding3), OnBoardingFragment(getString(R.string.msg_on_boarding4), R.drawable.ic_on_boarding4))

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}