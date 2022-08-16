package com.mangpo.taste.view

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityEmailAuthBinding
import com.mangpo.taste.util.setKeyboardVisibilityEvent

class EmailAuthActivity : BaseActivity<ActivityEmailAuthBinding>(ActivityEmailAuthBinding::inflate), TextWatcher {
    override fun initAfterBinding() {
        this.setKeyboardVisibilityEvent(binding.emailAuthStepIv, binding.emailAuthNextBtn)  //키보드 감지해서 뷰 바꾸기

        setEventListener()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.emailAuthNextBtn.isEnabled = p0!!.isNotBlank()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setEventListener() {
        binding.emailAuthSendBtn.setOnClickListener {
            (it as AppCompatButton).text = getString(R.string.action_resend)
            it.background = ContextCompat.getDrawable(applicationContext, R.drawable.bg_gy03_12)
        }

        binding.emailAuthAuthenticationCodeEt.addTextChangedListener(this)

        binding.emailAuthNextBtn.setOnClickListener {
            startNextActivity(NicknameSettingActivity::class.java)
        }
    }
}