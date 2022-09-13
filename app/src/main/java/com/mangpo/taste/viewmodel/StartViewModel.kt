package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.domain.usecase.CreateUserUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.util.SpfUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(private val createUserUseCase: CreateUserUseCase): BaseViewModel() {
    private val _createUserResult: MutableLiveData<Boolean> = MutableLiveData()
    val createUserResult: LiveData<Boolean> get() = _createUserResult

    fun createUser(createUserReqEntity: CreateUserReqEntity) {
        callApi(
            { createUserUseCase.invoke(createUserReqEntity) },
            {
                when (it.code) {
                    201 -> {
                        SpfUtils.writeSpf("nickname", it.data!!.nickname)
                        SpfUtils.writeEncryptedSpf("email", it.data!!.email)
                        SpfUtils.writeSpf("startDayCnt", it.data!!.startDayCnt)
                        SpfUtils.writeSpf("isAlarmOn", it.data!!.isAlarmOn)
                        SpfUtils.writeSpf("alarmTime", it.data!!.alarmDate)

                        _createUserResult.postValue(true)
                    }
                    400 -> {
                        showToast(it.msg!!)
                        _createUserResult.postValue(false)
                    }
                    else -> _createUserResult.postValue(false)
                }
            },
            true
        )
    }
}