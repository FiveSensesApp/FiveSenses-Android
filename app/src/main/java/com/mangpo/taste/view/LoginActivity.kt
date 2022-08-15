package com.mangpo.taste.view

import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityLoginBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.view.model.OneBtnDialog
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), TextWatcher {
//    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var oneBtnDialogFragment: OneBtnDialogFragment

    override fun initAfterBinding() {
        setEventListener()
        setOneBtnDialogFragment()

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(
            this@LoginActivity,
            KeyboardVisibilityEventListener {
                val charactersIvParams = (binding.loginCharactersIv.layoutParams as ConstraintLayout.LayoutParams)
                val loginBtnParams = (binding.loginLoginBtn.layoutParams as ConstraintLayout.LayoutParams)
                if (it) {   //키보드 올라와 있으면
                    charactersIvParams.topMargin = convertDpToPx(applicationContext,43) //marginTop 43dp
                    loginBtnParams.bottomMargin = convertDpToPx(applicationContext, 15) //marginBottom 15dp

                    binding.loginPwTv.visibility = View.GONE
                    binding.loginWhatPwTv.visibility = View.GONE
                } else {    //키보드 내려와 있으면
                    charactersIvParams.topMargin = convertDpToPx(applicationContext,81) //marginTop 81dp
                    loginBtnParams.bottomMargin = convertDpToPx(applicationContext, 122) //marginBottom 122dp

                    binding.loginPwTv.visibility = View.VISIBLE
                    binding.loginWhatPwTv.visibility = View.VISIBLE
                }

                binding.loginCharactersIv.layoutParams = charactersIvParams
                binding.loginLoginBtn.layoutParams = loginBtnParams
            })

        binding.loginPwTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG    //비밀번호에 밑줄 긋기

        /*binding.loginTv.setOnClickListener {
//            mainViewModel.getBooksByName("R")

            startNextActivity(MainActivity::class.java)
        }*/
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
    }

    private fun setEventListener() {
        binding.loginPwEt.addTextChangedListener(this)

        //눈 이미지 누르고 있으면 비밀번호 보여주고 아니면 가리기
        binding.loginPwEyeIb.setOnTouchListener { view, motionEvent ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    (view as ImageButton).setImageResource(R.drawable.ic_eye_rd2_30)
                    binding.loginPwEt.setTextColor(ContextCompat.getColor(applicationContext, R.color.RD_2))
                    binding.loginPwEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                MotionEvent.ACTION_UP -> {
                    (view as ImageButton).setImageResource(R.drawable.ic_eye_gy04_30)
                    binding.loginPwEt.setTextColor(ContextCompat.getColor(applicationContext, R.color.GY_04))
                    binding.loginPwEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                else -> {

                }
            }

            false
        }

        //로그인 버튼 클릭 리스너
        binding.loginLoginBtn.setOnClickListener {
            startActivityWithClear(MainActivity::class.java)
        }

        //로그인 실패 다이얼로그 보여주기 위한 임시 코드
        binding.loginWhatPwTv.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putParcelable("data", OneBtnDialog(getString(R.string.title_login_fail), getString(R.string.msg_check_email_pw), getString(R.string.action_confirm), listOf(46, 10, 46, 12)))

            oneBtnDialogFragment.arguments = bundle
            oneBtnDialogFragment.show(supportFragmentManager, null)
        }

        //비밀번호 텍스트뷰 클릭 리스너 -> TempPwActivity 로 이동
        binding.loginPwTv.setOnClickListener {
            startNextActivity(TempPwActivity::class.java)
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
        /*mainViewModel.toast.observe(this@LoginActivity) {
            it.getContentIfNotHandled()?.let { value ->
                showToast(value)
            }
        }

        mainViewModel.isLoading.observe(this@LoginActivity) {
            if (it)
                Log.d("MainActivity", "isLoading Observe!! -> 로딩중")
            else
                Log.d("MainActivity", "isLoading Observe!! -> 로딩끝")
        }

        mainViewModel.books.observe(this@LoginActivity) {
            Log.d("MainActivity", "books Observe!! -> $it")
        }*/
    }
}