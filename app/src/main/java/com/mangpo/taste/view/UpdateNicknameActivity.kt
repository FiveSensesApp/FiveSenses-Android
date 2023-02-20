package com.mangpo.taste.view

import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseActivity
import com.mangpo.taste.databinding.ActivityUpdateNicknameBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.matchRegex
import com.mangpo.taste.viewmodel.UpdateNicknameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNicknameActivity : BaseActivity<ActivityUpdateNicknameBinding>(R.layout.activity_update_nickname), TextWatcher {
    val nicknameValidation: MutableLiveData<Boolean> = MutableLiveData(false)

    private val updateNicknameVm: UpdateNicknameViewModel by viewModels()

    override fun initAfterBinding() {
        binding.apply {
            activity = this@UpdateNicknameActivity
            this.updateNicknameVm = this@UpdateNicknameActivity.updateNicknameVm
            nickname = SpfUtils.getStrSpf("nickname")
        }
        setCommonObserver(listOf(updateNicknameVm))

        binding.updateNicknameNicknameEt.addTextChangedListener(this)

        observe()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        nicknameValidation.postValue((p0.toString()!=SpfUtils.getStrSpf("nickname")) && matchRegex(p0.toString(), Regex("^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]{2,10}\$")))
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun observe() {
        updateNicknameVm.updateNicknameResultCode.observe(this, Observer {
            if (it==200)
                finish()
        })
    }

    fun updateNickname(nickname: String) {
        hideKeyboard(binding.root)
        updateNicknameVm.updateNickname(nickname)
    }
}