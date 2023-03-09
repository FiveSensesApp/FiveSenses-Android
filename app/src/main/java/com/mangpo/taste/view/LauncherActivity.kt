package com.mangpo.taste.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.databinding.DataBindingUtil
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ActivityLauncherBinding
import com.mangpo.taste.util.SpfUtils

class LauncherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher)
        binding.apply {
            activity = this@LauncherActivity
        }

        setContentView(binding.root)

        val msgLoginText = SpannableString(getString(R.string.msg_login))
        msgLoginText.setSpan(UnderlineSpan(), 11, 14, 0)
        binding.launcherLoginTv.text = msgLoginText
    }

    fun goLoginActivity() {
        SpfUtils.writeSpf("onBoarding", true)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun goSignInActivity() {
        SpfUtils.writeSpf("onBoarding", true)
        startActivity(Intent(this, CreateAccountActivity::class.java))
    }
}