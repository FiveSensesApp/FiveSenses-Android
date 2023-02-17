package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseActivity
import com.mangpo.taste.databinding.ActivityCreateAccountBinding
import com.mangpo.taste.util.matchRegex
import com.mangpo.taste.viewmodel.CreateAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

@AndroidEntryPoint
class CreateAccountActivity : BaseActivity<ActivityCreateAccountBinding>(R.layout.activity_create_account), TextWatcher {
    private val createAccountVm: CreateAccountViewModel by viewModels()

    private var isAgree: Boolean = false
    private var miIbCheckState: Boolean = false

    private lateinit var checkPoliciesBottomSheetFragment: CheckPoliciesBottomSheetFragment

    var pw1EyeTouched: MutableLiveData<Boolean> = MutableLiveData(false)
    var pw1EyeIbVisibility: MutableLiveData<Int> = MutableLiveData(View.INVISIBLE)
    var pw2EyeTouched: MutableLiveData<Boolean> = MutableLiveData(false)
    var pw2EyeIbVisibility: MutableLiveData<Int> = MutableLiveData(View.INVISIBLE)
    var isKeyboardVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    var nextBtnEnable: MutableLiveData<Boolean> = MutableLiveData(false)
    var isAllChecked: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun initAfterBinding() {
        binding.apply {
            binding.activity = this@CreateAccountActivity
            this.createAccountVm = this@CreateAccountActivity.createAccountVm
        }
        setCommonObserver(listOf(createAccountVm))

        initCheckPoliciesBottomSheetFragment()

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(this@CreateAccountActivity) {
            isKeyboardVisible.postValue(it)

            binding.createAccountPw1Et.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.createAccountPw2Et.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        setEventListener()
        observe()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (binding.createAccountPw1Et.hasFocus()) {
            if (p0!!.isBlank()) {
                pw1EyeIbVisibility.postValue(View.INVISIBLE)
            } else {
                pw1EyeIbVisibility.postValue(View.VISIBLE)
            }
        } else {
            if (p0!!.isBlank()) {
                pw2EyeIbVisibility.postValue(View.INVISIBLE)
            } else {
                pw2EyeIbVisibility.postValue(View.VISIBLE)
            }
        }

        showToast(validate().toString())
        nextBtnEnable.postValue(validate())
    }

    override fun afterTextChanged(p0: Editable?) {
        binding.createAccountPw1Et.typeface = binding.createAccountEmailEt.typeface //패스워드 EditText 글꼴이 계속 풀리는 문제 해결
        binding.createAccountPw2Et.typeface = binding.createAccountEmailEt.typeface //패스워드 EditText 글꼴이 계속 풀리는 문제 해결
    }

    private fun initCheckPoliciesBottomSheetFragment() {
        checkPoliciesBottomSheetFragment = CheckPoliciesBottomSheetFragment()
        checkPoliciesBottomSheetFragment.setMyListener(object : CheckPoliciesBottomSheetFragment.Listener {
            override fun finish(allChecked: Boolean, agree: Boolean, miIbCheckState: Boolean) {
                isAllChecked.postValue(allChecked)
                showToast(validate().toString())
                nextBtnEnable.postValue(validate())
                isAgree = agree
                this@CreateAccountActivity.miIbCheckState = miIbCheckState
            }
        })
    }

    private fun setEventListener() {
        binding.createAccountPw1Et.addTextChangedListener(this)
        binding.createAccountPw2Et.addTextChangedListener(this)

        //눈 이미지 누르고 있으면 비밀번호 보여주고 아니면 가리기
        binding.createAccountPw1EyeIb.setOnTouchListener { view, motionEvent ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.createAccountPw1Et.inputType = InputType.TYPE_CLASS_TEXT
                    pw1EyeTouched.postValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    binding.createAccountPw1Et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    pw1EyeTouched.postValue(false)
                }
                else -> {}
            }

            false
        }

        //눈 이미지 누르고 있으면 비밀번호 보여주고 아니면 가리기
        binding.createAccountPw2EyeIb.setOnTouchListener { view, motionEvent ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.createAccountPw2Et.inputType = InputType.TYPE_CLASS_TEXT
                    pw2EyeTouched.postValue(true)
                }
                MotionEvent.ACTION_UP -> {
                    binding.createAccountPw2Et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    pw2EyeTouched.postValue(false)
                }
                else -> {}
            }

            false
        }
    }

    //다음 버튼 활성화/비활성화 여부를 위한 유효성 검사 함수
    private fun validate(): Boolean {
        return binding.createAccountEmailEt.text.isNotBlank() &&
                binding.createAccountPw1Et.text.isNotBlank() &&
                binding.createAccountPw2Et.text.isNotBlank() &&
                matchRegex(binding.createAccountEmailEt.text.toString(), Patterns.EMAIL_ADDRESS.toRegex()) &&
                validatePw(binding.createAccountPw1Et.text.toString()) &&
                binding.createAccountPw1Et.text.toString()==binding.createAccountPw2Et.text.toString() &&
                isAgree
    }

    private fun validatePw(pw: String): Boolean {
        if (pw.length < 10) {
            return false
        }

        //특수문자는 !@#$&* 만 가능
        val notPermitChar = pw.replace("[\\da-zA-Z!@#\$&*]".toRegex(), "")
        if (notPermitChar.isNotBlank()) {
            return false
        }

        return matchRegex(pw, "^(?=.+[a-zA-Z0-9])(?=.+[a-zA-Z!@#\$&*])(?=.+[0-9!@#\$&*])(?=.+[0-9a-zA-Z!@#\$&*]).{10,}\$".toRegex())
    }

    private fun observe() {
        createAccountVm.validateDuplicateResult.observe(this, Observer {
            if (it) {
                val createUserReqEntity: CreateUserReqEntity = CreateUserReqEntity(email = binding.createAccountEmailEt.text.toString(), password = binding.createAccountPw1Et.text.toString(), isMarketingAllowed = miIbCheckState)
                val intent = Intent(this, EmailAuthActivity::class.java)
                intent.putExtra("newUser", createUserReqEntity)
                startActivity(intent)
            }
        })
    }

    //가입 약관 동의 체크 이벤트 함수
    fun changeCheckIb() {
        isAllChecked.postValue(!(isAllChecked.value?: false))
        isAgree = isAllChecked.value?: false
        miIbCheckState = true
        nextBtnEnable.postValue(validate())
        showToast(validate().toString())
    }
    /*fun changeCheckIb() {
        isAllChecked.postValue(!(isAllChecked.value?: false))
        showToast(validate().toString())
        nextBtnEnable.postValue(validate())
        isAgree = isAllChecked.value?: false
        miIbCheckState = true
    }*/

    fun showPoliciesBottomSheet() {
        val bundle: Bundle = Bundle()
        bundle.putBoolean("isAllChecked", isAllChecked.value?: false)
        bundle.putBoolean("isAgree", isAgree)
        checkPoliciesBottomSheetFragment.arguments = bundle
        checkPoliciesBottomSheetFragment.show(supportFragmentManager, null)
    }

    fun validateDuplicate(email: String) {
        hideKeyboard(binding.root)
        createAccountVm.validateDuplicate(email)
    }
}
/*
class CreateAccountActivity : BaseActivity<ActivityCreateAccountBinding, CreateAccountViewModel>(ActivityCreateAccountBinding::inflate), TextWatcher {
    override val viewModel: CreateAccountViewModel by viewModels()

    private var isAgree: Boolean = false
    private var miIbCheckState: Boolean = false

    private lateinit var checkPoliciesBottomSheetFragment: CheckPoliciesBottomSheetFragment

    var pw1EyeTouched: Boolean = false
    var pw1EyeIbVisibility: Int = View.INVISIBLE
    var pw2EyeTouched: Boolean = false
    var pw2EyeIbVisibility: Int = View.INVISIBLE
    var isKeyboardVisible: Boolean = false
    var nextBtnEnable: Boolean = false
    var isAllChecked: Boolean = false

    override fun initAfterBinding() {
        binding.apply {
            binding.activity = this@CreateAccountActivity
        }

        initCheckPoliciesBottomSheetFragment()

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

        observe()
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

    private fun initCheckPoliciesBottomSheetFragment() {
        checkPoliciesBottomSheetFragment = CheckPoliciesBottomSheetFragment()
        checkPoliciesBottomSheetFragment.setMyListener(object : CheckPoliciesBottomSheetFragment.Listener {
            override fun finish(allChecked: Boolean, agree: Boolean, miIbCheckState: Boolean) {
                isAllChecked = allChecked
                isAgree = agree
                this@CreateAccountActivity.miIbCheckState = miIbCheckState
                nextBtnEnable = validate()
                binding.invalidateAll()
            }
        })
    }

    private fun setEventListener() {
        binding.createAccountPw1Et.addTextChangedListener(this)
        binding.createAccountPw2Et.addTextChangedListener(this)

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
                validatePw(binding.createAccountPw1Et.text.toString()) &&
                binding.createAccountPw1Et.text.toString()==binding.createAccountPw2Et.text.toString() &&
                isAgree
    }

    private fun validatePw(pw: String): Boolean {
        if (pw.length < 10) {
            return false
        }

        //특수문자는 !@#$&* 만 가능
        val notPermitChar = pw.replace("[\\da-zA-Z!@#\$&*]".toRegex(), "")
        if (notPermitChar.isNotBlank()) {
            return false
        }

        return matchRegex(pw, "^(?=.+[a-zA-Z0-9])(?=.+[a-zA-Z!@#\$&*])(?=.+[0-9!@#\$&*])(?=.+[0-9a-zA-Z!@#\$&*]).{10,}\$".toRegex())
    }

    private fun observe() {
        viewModel.validateDuplicateResult.observe(this, Observer {
            if (it) {
                val createUserReqEntity: CreateUserReqEntity = CreateUserReqEntity(email = binding.createAccountEmailEt.text.toString(), password = binding.createAccountPw1Et.text.toString(), isMarketingAllowed = miIbCheckState)
                val intent = Intent(this, EmailAuthActivity::class.java)
                intent.putExtra("newUser", createUserReqEntity)
                startActivity(intent)
            }
        })
    }

    //가입 약관 동의 체크 이벤트 함수
    fun changeCheckIb() {
        isAllChecked = !isAllChecked
        isAgree = isAllChecked
        miIbCheckState = true
        nextBtnEnable = validate()
        binding.invalidateAll()
    }

    fun showPoliciesBottomSheet() {
        val bundle: Bundle = Bundle()
        bundle.putBoolean("isAllChecked", isAllChecked)
        bundle.putBoolean("isAgree", isAgree)
        checkPoliciesBottomSheetFragment.arguments = bundle
        checkPoliciesBottomSheetFragment.show(supportFragmentManager, null)
    }

    fun validateDuplicate(email: String) {
        hideKeyboard(binding.root)
        viewModel.validateDuplicate(email)
    }
}*/
