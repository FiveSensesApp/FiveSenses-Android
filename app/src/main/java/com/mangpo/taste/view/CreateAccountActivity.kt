package com.mangpo.taste.view

import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityCreateAccountBinding
import com.mangpo.taste.util.setKeyboardVisibilityEvent

class CreateAccountActivity : BaseActivity<ActivityCreateAccountBinding>(ActivityCreateAccountBinding::inflate) {
    override fun initAfterBinding() {
        this.setKeyboardVisibilityEvent(binding.createAccountStepIv, binding.createAccountNextBtn)  //키보드 감지해서 뷰 바꾸기

        setEventListener()
    }

    private fun setEventListener() {
        binding.createAccountNextBtn.setOnClickListener {
            startNextActivity(EmailAuthActivity::class.java)
        }
    }
}