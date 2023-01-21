package com.mangpo.taste.view

import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityUpdateNicknameBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.matchRegex
import com.mangpo.taste.viewmodel.UpdateNicknameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNicknameActivity : BaseActivity<ActivityUpdateNicknameBinding, UpdateNicknameViewModel>(ActivityUpdateNicknameBinding::inflate), TextWatcher {
    override val viewModel: UpdateNicknameViewModel by viewModels()

    override fun initAfterBinding() {
        binding.apply {
            activity = this@UpdateNicknameActivity
            nickname = SpfUtils.getStrSpf("nickname")
        }

        binding.updateNicknameNicknameEt.addTextChangedListener(this)

        observe()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.isValid = (p0.toString()!=SpfUtils.getStrSpf("nickname")) && matchRegex(p0.toString(), Regex("^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]{2,10}\$"))
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun observe() {
        viewModel.updateNicknameResultCode.observe(this, Observer {
            if (it==200)
                finish()
        })
    }

    fun back() {
        finish()
    }

    fun updateNickname(nickname: String) {
        hideKeyboard(binding.root)
        viewModel.updateNickname(nickname)
    }
}