package com.mangpo.taste.view

import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseActivity
import com.mangpo.taste.databinding.ActivityNicknameSettingBinding
import com.mangpo.taste.util.matchRegex
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class NicknameSettingActivity : BaseActivity<ActivityNicknameSettingBinding>(R.layout.activity_nickname_setting), TextWatcher {
    val isKeyboardVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private lateinit var createUserReqEntity: CreateUserReqEntity

    override fun initAfterBinding() {
        binding.apply {
            activity = this@NicknameSettingActivity
        }

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(this@NicknameSettingActivity) {
            isKeyboardVisible.postValue(it)
        }

        createUserReqEntity = intent.getParcelableExtra<CreateUserReqEntity>("newUser")!!

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
    }

    private fun setInfoTVDesign(text: Int, color: Int) {
        binding.nicknameSettingInfoTv.setTextColor(ContextCompat.getColor(applicationContext, color))
        binding.nicknameSettingInfoTv.text = getString(text)
    }

    fun goNextActivity(view: EditText) {
        createUserReqEntity.nickname = view.text.toString()

        //알람 권한 체크(Android 13 Version 이상)
        var intent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(this, AlarmTimeSettingActivity::class.java)
        } else {
            Intent(this, StartActivity::class.java)
        }

        intent.putExtra("newUser", createUserReqEntity)
        startActivity(intent)
    }
}