package com.mangpo.taste.view

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseActivity
import com.mangpo.taste.databinding.ActivityEmailAuthBinding
import com.mangpo.taste.viewmodel.EmailAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

@AndroidEntryPoint
class EmailAuthActivity : BaseActivity<ActivityEmailAuthBinding>(R.layout.activity_email_auth), TextWatcher {
    private val emailAuthVm: EmailAuthViewModel by viewModels()

    var isKeyboardVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var email: String = ""

    private lateinit var createUserReqEntity: CreateUserReqEntity

    override fun initAfterBinding() {
        createUserReqEntity = intent.getParcelableExtra<CreateUserReqEntity>("newUser")!!

        binding.apply {
            activity = this@EmailAuthActivity
            this.emailAuthVm = this@EmailAuthActivity.emailAuthVm
            email = createUserReqEntity.email
        }
        setCommonObserver(listOf(emailAuthVm))

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(this@EmailAuthActivity) {
            isKeyboardVisible.postValue(it)
        }

        setEventListener()
        observe()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.emailAuthNextBtn.isEnabled = p0!!.isNotBlank()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setEventListener() {
        binding.emailAuthAuthenticationCodeEt.addTextChangedListener(this)
    }

    private fun observe() {
        emailAuthVm.validateResultCode.observe(this, Observer {
            if (it==200) {
                val intent = Intent(this, NicknameSettingActivity::class.java)
                intent.putExtra("newUser", createUserReqEntity)
                startActivity(intent)
            }
        })
    }
}