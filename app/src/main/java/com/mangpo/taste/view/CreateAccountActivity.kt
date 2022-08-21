package com.mangpo.taste.view

import android.text.Editable
import android.text.TextWatcher
import androidx.constraintlayout.widget.ConstraintLayout
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityCreateAccountBinding
import com.mangpo.taste.util.convertDpToPx
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import com.mangpo.taste.util.setKeyboardVisibilityEvent

class CreateAccountActivity : BaseActivity<ActivityCreateAccountBinding>(ActivityCreateAccountBinding::inflate), TextWatcher {
    var pw1EyeTouched: Boolean = false
    var pw2EyeTouched: Boolean = false

    override fun initAfterBinding() {
        binding.data = this

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(
            this@CreateAccountActivity,
            KeyboardVisibilityEventListener {
                val stepsIvParams = (binding.createAccountStepIv.layoutParams as ConstraintLayout.LayoutParams)
                val nextBtnParams = (binding.createAccountNextBtn.layoutParams as ConstraintLayout.LayoutParams)
                if (it) {   //키보드 올라와 있으면
                    stepsIvParams.topMargin = convertDpToPx(applicationContext,36) //marginTop 36dp
                    nextBtnParams.bottomMargin = convertDpToPx(applicationContext, 15) //marginBottom 15dp
                } else {    //키보드 내려와 있으면
                    stepsIvParams.topMargin = convertDpToPx(applicationContext,74) //marginTop 74dp
                    nextBtnParams.bottomMargin = convertDpToPx(applicationContext, 122) //marginBottom 122dp
                }

                binding.createAccountStepIv.layoutParams = stepsIvParams
                binding.createAccountNextBtn.layoutParams = nextBtnParams
            })
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (binding.createAccountPw1Et.hasFocus()) {

        } else {

        }
    }

    override fun afterTextChanged(p0: Editable?) {
        binding.createAccountPw1Et.typeface = binding.createAccountEmailEt.typeface //패스워드 EditText 글꼴이 계속 풀리는 문제 해결
        binding.createAccountPw2Et.typeface = binding.createAccountEmailEt.typeface //패스워드 EditText 글꼴이 계속 풀리는 문제 해결
        this.setKeyboardVisibilityEvent(binding.createAccountStepIv, binding.createAccountNextBtn)  //키보드 감지해서 뷰 바꾸기

        setEventListener()
    }

    private fun setEventListener() {
        binding.createAccountNextBtn.setOnClickListener {
            startNextActivity(EmailAuthActivity::class.java)
        }
    }
}