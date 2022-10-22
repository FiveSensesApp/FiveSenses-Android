package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.changePassword.ChangePasswordReqEntity
import com.mangpo.domain.usecase.ChangePasswordUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PwResettingViewModel @Inject constructor(private val changePasswordUseCase: ChangePasswordUseCase): BaseViewModel() {
    private val _changePasswordResult: MutableLiveData<Int> = MutableLiveData()
    val changePasswordResult: LiveData<Int> get() = _changePasswordResult

    var checkOldPwMsg: MutableLiveData<String> = MutableLiveData(null)

    fun changePassword(oldPw: String, newPw: String) {
        callApi(
            { changePasswordUseCase.invoke(ChangePasswordReqEntity(newPw, oldPw)) },
            {
                when (it.code) {
                    200 -> { }
                    400 -> { checkOldPwMsg.postValue(it.msg) }
                }
                _changePasswordResult.postValue(it.code)
            },
            true
        )
    }
}