package com.mangpo.taste.view

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityEmailAuthBinding
import com.mangpo.taste.viewmodel.EmailAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

@AndroidEntryPoint
class EmailAuthActivity : BaseActivity<ActivityEmailAuthBinding>(ActivityEmailAuthBinding::inflate), TextWatcher {
    private val emailAuthVm: EmailAuthViewModel by viewModels()

    var isKeyboardVisible: Boolean = false
    var email: String = ""

    private lateinit var createUserReqEntity: CreateUserReqEntity

    override fun initAfterBinding() {
        binding.apply {
            activity = this@EmailAuthActivity
            vm = emailAuthVm
            lifecycleOwner = this@EmailAuthActivity
        }

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(
            this@EmailAuthActivity,
            KeyboardVisibilityEventListener {
                isKeyboardVisible = it
                binding.invalidateAll()
            })

        setEventListener()
        observe()

        createUserReqEntity = intent.getParcelableExtra<CreateUserReqEntity>("newUser")!!
        email = createUserReqEntity.email
        binding.invalidateAll()
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
        emailAuthVm.toast.observe(this, Observer {
            val msg = it.getContentIfNotHandled()

            if (msg!=null)
                showToast((msg))
        })

        emailAuthVm.validateResultCode.observe(this, Observer {
            if (it==200) {
                val intent = Intent(this, NicknameSettingActivity::class.java)
                intent.putExtra("newUser", createUserReqEntity)
                startActivity(intent)
            }
        })
    }
}