package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Patterns
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityTempPwBinding
import com.mangpo.taste.util.matchRegex
import com.mangpo.taste.view.model.OneBtnDialog
import com.mangpo.taste.view.model.TwoBtnDialog
import com.mangpo.taste.viewmodel.TempPwViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

@AndroidEntryPoint
class TempPwActivity : BaseActivity<ActivityTempPwBinding, TempPwViewModel>(ActivityTempPwBinding::inflate) {
    override val viewModel: TempPwViewModel by viewModels()

    private lateinit var oneBtnDialogFragment: OneBtnDialogFragment
    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment

    var isKeyboardShown: Boolean = false

    override fun initAfterBinding() {
        binding.apply {
            activity = this@TempPwActivity
            vm = viewModel
            lifecycleOwner = this@TempPwActivity
        }

        val dontKnowEmailTvText = SpannableString(binding.tempPwDontKnowEmailTv.text.toString())
        dontKnowEmailTvText.setSpan(UnderlineSpan(), 0, 3, 0)
        binding.tempPwDontKnowEmailTv.text = dontKnowEmailTvText

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(
            this@TempPwActivity,
            KeyboardVisibilityEventListener {
                isKeyboardShown = it
                binding.invalidateAll()
            })

        initDialog()
        observe()
    }

    private fun initDialog() {
        oneBtnDialogFragment = OneBtnDialogFragment()
        oneBtnDialogFragment.setMyCallback(object: OneBtnDialogFragment.MyCallback {
            override fun end() {
                finish()
            }
        })

        twoBtnDialogFragment = TwoBtnDialogFragment()
        twoBtnDialogFragment.setMyCallback(object : TwoBtnDialogFragment.MyCallback {
            override fun leftAction(action: String) { //뒤로 가기
            }

            override fun rightAction(action: String) {    //문의 메일
                val emailIntent: Intent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "message/rfc822"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.app_email)))
                startActivity(emailIntent)
            }
        })
    }

    private fun observe() {
        viewModel.lostPasswordResCode.observe(this, Observer {
            if (it==200) {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", OneBtnDialog(getString(R.string.title_temporary_pw_issued), getString(R.string.msg_reset_pw), getString(R.string.action_confirm), listOf(46, 10, 46, 12)))

                oneBtnDialogFragment.arguments = bundle
                oneBtnDialogFragment.show(supportFragmentManager, null)
            }
        })
    }

    fun lostPassword(email: String) {
        hideKeyboard(binding.root)  //키보드 내리기

        if (email.isBlank()) {  //빈값일 때
            showToast("이메일을 입력해주세요.")
        } else if (!matchRegex(email, Patterns.EMAIL_ADDRESS.toRegex())) {  //이메일 형식이 아닐 때
            showToast("이메일 형식이 아닙니다.")
        } else {
            viewModel.lostPassword(binding.tempPwEmailEt.text.toString())
        }
    }

    fun showTwoBtnDialog() {
        val bundle: Bundle = Bundle()
        bundle.putParcelable("data", TwoBtnDialog(getString(R.string.title_find_email), getString(R.string.msg_contact_official_email), getString(R.string.action_go_back), getString(R.string.title_inquiry_mail), R.drawable.bg_gy01_12))

        twoBtnDialogFragment.arguments = bundle
        twoBtnDialogFragment.show(supportFragmentManager, null)
    }
}