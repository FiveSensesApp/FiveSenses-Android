package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.usecase.ValidateEmailSendCodeUseCase
import com.mangpo.domain.usecase.ValidateEmailUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailAuthViewModel @Inject constructor(private val validateEmailUseCase: ValidateEmailUseCase, private val validateEmailSendCodeUseCase: ValidateEmailSendCodeUseCase): BaseViewModel() {
    var isSendBtnClicked: MutableLiveData<Boolean> = MutableLiveData(false)
    var validateErrMsg: MutableLiveData<String> = MutableLiveData("")

    private val _validateResultCode: MutableLiveData<Int> = MutableLiveData()
    val validateResultCode: LiveData<Int> get() = _validateResultCode

    fun validateEmail(email: String) {
        isSendBtnClicked.postValue(true)

        callApi(
            { validateEmailUseCase.invoke(email) },
            {  },
            true
        )
    }

    fun validateEmailSendCode(email: String, emailCode: Int) {
        callApi(
            { validateEmailSendCodeUseCase.invoke(email, emailCode) },
            {
                _validateResultCode.postValue(it.code)

                when (it.code) {
                    200 -> {}
                    400 -> { validateErrMsg.postValue(it.msg) }
                    else -> {}
                }
            }
        )
    }
}