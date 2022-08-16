package com.mangpo.taste.view

import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityNicknameSettingBinding
import com.mangpo.taste.util.matchRegex
import com.mangpo.taste.util.setKeyboardVisibilityEvent

class NicknameSettingActivity : BaseActivity<ActivityNicknameSettingBinding>(ActivityNicknameSettingBinding::inflate), TextWatcher {
    override fun initAfterBinding() {
        this.setKeyboardVisibilityEvent(binding.nicknameSettingStepIv, binding.nicknameSettingNextBtn)  //키보드 감지해서 뷰 바꾸기

        setEventListener()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0!!.isBlank()) {   //비어있을 때
            binding.nicknameSettingNextBtn.isEnabled = false
            setInfoTVDesign(R.string.msg_nickname_conditions, R.color.GY_04)
        } else {    //비어있지 않을 때
            if (matchRegex(p0.toString(), "[가-힣a-zA-Z0-9 ]+".toRegex())) {  //한글, 영어, 숫자만 있을 때
                binding.nicknameSettingNextBtn.isEnabled = true
                setInfoTVDesign(R.string.msg_nickname_conditions, R.color.GY_04)
            } else {    //이모티콘, 특수문자가 있을 때
                binding.nicknameSettingNextBtn.isEnabled = false
                setInfoTVDesign(R.string.error_contains_unacceptable_characters, R.color.RD_2)
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setEventListener() {
        binding.nicknameSettingNicknameEt.addTextChangedListener(this)

        binding.nicknameSettingNextBtn.setOnClickListener {
            startNextActivity(AlarmTimeSettingActivity::class.java)
        }
    }

    private fun setInfoTVDesign(text: Int, color: Int) {
        binding.nicknameSettingInfoTv.setTextColor(ContextCompat.getColor(applicationContext, color))
        binding.nicknameSettingInfoTv.text = getString(text)
    }
}