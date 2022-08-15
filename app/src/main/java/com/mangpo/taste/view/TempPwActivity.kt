package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityTempPwBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.view.model.OneBtnDialog
import com.mangpo.taste.view.model.TwoBtnDialog
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class TempPwActivity : BaseActivity<ActivityTempPwBinding>(ActivityTempPwBinding::inflate), TextWatcher {
    private lateinit var oneBtnDialogFragment: OneBtnDialogFragment
    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment

    override fun initAfterBinding() {
        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(
            this@TempPwActivity,
            KeyboardVisibilityEventListener {
                val charactersIvParams = (binding.tempPwCharactersIv.layoutParams as ConstraintLayout.LayoutParams)
                val sendMailBtnParams = (binding.tempPwSendMailBtn.layoutParams as ConstraintLayout.LayoutParams)
                if (it) {   //키보드 올라와 있으면
                    charactersIvParams.topMargin = convertDpToPx(applicationContext,43) //marginTop 43dp
                    sendMailBtnParams.bottomMargin = convertDpToPx(applicationContext, 15) //marginBottom 15dp

                    binding.tempPwDontKnowEmailTv.visibility = View.GONE
                } else {    //키보드 내려와 있으면
                    charactersIvParams.topMargin = convertDpToPx(applicationContext,81) //marginTop 81dp
                    sendMailBtnParams.bottomMargin = convertDpToPx(applicationContext, 122) //marginBottom 122dp

                    binding.tempPwDontKnowEmailTv.visibility = View.VISIBLE
                }

                binding.tempPwCharactersIv.layoutParams = charactersIvParams
                binding.tempPwSendMailBtn.layoutParams = sendMailBtnParams
            })

        setEventListener()
        initDialog()

        val dontKnowEmailTvText = SpannableString(binding.tempPwDontKnowEmailTv.text.toString())
        dontKnowEmailTvText.setSpan(UnderlineSpan(), 0, 3, 0)
        binding.tempPwDontKnowEmailTv.text = dontKnowEmailTvText
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.tempPwSendMailBtn.isEnabled = p0!!.isNotBlank()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setEventListener() {
        binding.tempPwEmailEt.addTextChangedListener(this)

        binding.tempPwSendMailBtn.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putParcelable("data", OneBtnDialog(getString(R.string.title_temporary_pw_issued), getString(R.string.msg_reset_pw), getString(R.string.action_confirm), listOf(46, 10, 46, 12)))

            oneBtnDialogFragment.arguments = bundle
            oneBtnDialogFragment.show(supportFragmentManager, null)
        }

        binding.tempPwDontKnowEmailTv.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putParcelable("data", TwoBtnDialog(getString(R.string.title_find_email), getString(R.string.msg_contact_official_email), getString(R.string.action_go_back), getString(R.string.title_inquiry_mail), R.drawable.bg_gy01_12))

            twoBtnDialogFragment.arguments = bundle
            twoBtnDialogFragment.show(supportFragmentManager, null)
        }
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
            override fun leftAction() { //뒤로 가기
            }

            override fun rightAction() {    //문의 메일
                val emailIntent: Intent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "message/rfc822"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.app_email)))
                startActivity(emailIntent)
            }
        })
    }
}