package com.mangpo.taste.view

import android.content.Intent
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityCreateAccountBinding
import com.mangpo.taste.util.matchRegex
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class CreateAccountActivity : BaseActivity<ActivityCreateAccountBinding>(ActivityCreateAccountBinding::inflate), TextWatcher {
    var pw1EyeTouched: Boolean = false
    var pw1EyeIbVisibility: Int = View.INVISIBLE
    var pw2EyeTouched: Boolean = false
    var pw2EyeIbVisibility: Int = View.INVISIBLE
    var isKeyboardVisible: Boolean = false
    var nextBtnEnable: Boolean = false
    var isIbChecked: Boolean = false

    override fun initAfterBinding() {
        binding.activity = this

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(
            this@CreateAccountActivity,
            KeyboardVisibilityEventListener {
                isKeyboardVisible = it
                binding.invalidateAll()

                binding.createAccountPw1Et.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.createAccountPw2Et.transformationMethod = PasswordTransformationMethod.getInstance()
            })

        setEventListener()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (binding.createAccountPw1Et.hasFocus()) {
            pw1EyeIbVisibility = if (p0!!.isBlank()) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
        } else {
            pw2EyeIbVisibility = if (p0!!.isBlank()) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
        }
        nextBtnEnable = validate()

        binding.invalidateAll()
    }

    override fun afterTextChanged(p0: Editable?) {
        binding.createAccountPw1Et.typeface = binding.createAccountEmailEt.typeface //패스워드 EditText 글꼴이 계속 풀리는 문제 해결
        binding.createAccountPw2Et.typeface = binding.createAccountEmailEt.typeface //패스워드 EditText 글꼴이 계속 풀리는 문제 해결
    }

    private fun setEventListener() {
        binding.createAccountPw1Et.addTextChangedListener(this)
        binding.createAccountPw2Et.addTextChangedListener(this)

        binding.createAccountNextBtn.setOnClickListener {
            val createUserReqEntity: CreateUserReqEntity = CreateUserReqEntity(email = binding.createAccountEmailEt.text.toString(), password = binding.createAccountPw1Et.text.toString())
            val intent = Intent(this, EmailAuthActivity::class.java)
            intent.putExtra("newUser", createUserReqEntity)
            startActivity(intent)
        }

        //눈 이미지 누르고 있으면 비밀번호 보여주고 아니면 가리기
        binding.createAccountPw1EyeIb.setOnTouchListener { view, motionEvent ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.createAccountPw1Et.inputType = InputType.TYPE_CLASS_TEXT
                    pw1EyeTouched = true
                }
                MotionEvent.ACTION_UP -> {
                    binding.createAccountPw1Et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    pw1EyeTouched = false
                }
                else -> {}
            }

            binding.invalidateAll()

            false
        }

        //눈 이미지 누르고 있으면 비밀번호 보여주고 아니면 가리기
        binding.createAccountPw2EyeIb.setOnTouchListener { view, motionEvent ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.createAccountPw2Et.inputType = InputType.TYPE_CLASS_TEXT
                    pw2EyeTouched = true
                }
                MotionEvent.ACTION_UP -> {
                    binding.createAccountPw2Et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    pw2EyeTouched = false
                }
                else -> {}
            }

            binding.invalidateAll()

            false
        }
    }

    //다음 버튼 활성화/비활성화 여부를 위한 유효성 검사 함수
    private fun validate(): Boolean {
        return binding.createAccountEmailEt.text.isNotBlank() &&
                binding.createAccountPw1Et.text.isNotBlank() &&
                binding.createAccountPw2Et.text.isNotBlank() &&
                matchRegex(binding.createAccountEmailEt.text.toString(), Patterns.EMAIL_ADDRESS.toRegex()) &&
                matchRegex(binding.createAccountPw1Et.text.toString(), "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{10,30}\$".toRegex()) &&
                matchRegex(binding.createAccountPw2Et.text.toString(), "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{10,30}\$".toRegex()) &&
                binding.createAccountPw1Et.text.toString()==binding.createAccountPw2Et.text.toString() &&
                isIbChecked
    }

    //가입 약관 동의 체크 이벤트 함수
    fun changeCheckIb() {
        isIbChecked = !isIbChecked
        nextBtnEnable = validate()
        binding.invalidateAll()
    }

    fun showPoliciesBottomSheet() {
        val policiesBottomSheetFragment = CheckPoliciesBottomSheetFragment()
        policiesBottomSheetFragment.show(supportFragmentManager, null)
    }
}