package com.mangpo.taste.view

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mangpo.domain.model.authorizeNew.AuthorizeNewReqEntity
import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseActivity
import com.mangpo.taste.databinding.ActivityLoginBinding
import com.mangpo.taste.view.model.OneBtnDialog
import com.mangpo.taste.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login), TextWatcher {
    val viewModel: LoginViewModel by viewModels()

    var eyeTouchedStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    var isKeyboardVisible: MutableLiveData<Boolean> = MutableLiveData(false)

    private lateinit var oneBtnDialogFragment: OneBtnDialogFragment

    override fun initAfterBinding() {
        binding.apply {
            activity = this@LoginActivity
            loginVm = viewModel
        }
        setCommonObserver(listOf(viewModel))

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(this@LoginActivity) {
            isKeyboardVisible.postValue(it)
            binding.loginPwEt.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        setEventListener()
        setOneBtnDialogFragment()
        observe()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.loginLoginBtn.isEnabled = validate()    //이메일 주소랑 비밀번호가 모두 입력됐는지 유효성 검사

        //비밀번호 입력중이면 눈 이미지버튼 visible 아니면 invisible
        if (binding.loginPwEt.hasFocus()) {
            if (p0!!.isEmpty())
                binding.loginPwEyeIb.visibility = View.INVISIBLE
            else
                binding.loginPwEyeIb.visibility = View.VISIBLE
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        binding.loginPwEt.typeface = binding.loginEmailEt.typeface  //패스워드 EditText 글꼴이 계속 풀리는 문제 해결
    }

    private fun setEventListener() {
        binding.loginPwEt.addTextChangedListener(this)

        //눈 이미지 누르고 있으면 비밀번호 보여주고 아니면 가리기
        binding.loginPwEyeIb.setOnTouchListener { view, motionEvent ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.loginPwEt.inputType = InputType.TYPE_CLASS_TEXT
                    eyeTouchedStatus.postValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    binding.loginPwEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    eyeTouchedStatus.postValue(false)
                }
                else -> {}
            }

            false
        }
    }

    private fun setOneBtnDialogFragment() {
        oneBtnDialogFragment = OneBtnDialogFragment()
        oneBtnDialogFragment.setMyCallback(object: OneBtnDialogFragment.MyCallback {
            override fun end() {
            }
        })
    }

    private fun validate(): Boolean = binding.loginEmailEt.text.isNotBlank() && binding.loginPwEt.text.isNotBlank()

    private fun observe() {
        viewModel.loginSuccess.observe(this, Observer {
            if (it) {   //로그인 성공
                startActivityWithClear(MainActivity::class.java)
            } else {    //로그인 실패
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", OneBtnDialog(getString(R.string.title_login_fail), getString(R.string.msg_check_email_pw), getString(R.string.action_confirm), listOf(46, 10, 46, 12)))

                oneBtnDialogFragment.arguments = bundle
                oneBtnDialogFragment.show(supportFragmentManager, null)
            }
        })
    }

    //로그인 버튼 클릭 리스너
    fun login(view: AppCompatButton) {
        hideKeyboard(binding.root)
        viewModel.authorizeNew(AuthorizeNewReqEntity(binding.loginEmailEt.text.toString(), binding.loginPwEt.text.toString()))
    }

    fun goFindPwActivity(view: TextView) {
        startNextActivity(TempPwActivity::class.java)
    }
}