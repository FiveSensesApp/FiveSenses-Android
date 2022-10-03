package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.updateUser.UpdateUserReqEntity
import com.mangpo.domain.usecase.DeleteUserUseCase
import com.mangpo.domain.usecase.UpdateUserUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.base.Event
import com.mangpo.taste.util.SpfUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val deleteUserUseCase: DeleteUserUseCase, private val updateUserUseCase: UpdateUserUseCase): BaseViewModel() {
    private val _deleteUserResultCode: MutableLiveData<Int> = MutableLiveData()
    val deleteUserResultCode: LiveData<Int> get() = _deleteUserResultCode

    private val _updateUserResultCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val updateUserResultCode: LiveData<Event<Int>> get() = _updateUserResultCode

    fun deleteUser(userId: Int) {
        callApi(
            { deleteUserUseCase.invoke(userId) },
            {
                _deleteUserResultCode.postValue(it.code)

                when (it.code) {
                    404 -> showToast(it.msg!!)
                }
            },
            true
        )
    }

    fun updateUser(updateUserReqEntity: UpdateUserReqEntity) {
        callApi(
            { updateUserUseCase.invoke(updateUserReqEntity) },
            {
                when (it.code) {
                    200 -> {
                        SpfUtils.writeSpf("alarmTime", updateUserReqEntity.alarmDate)
                        SpfUtils.writeSpf("isAlarmOn", updateUserReqEntity.isAlarmOn)
                    }
                    404 -> showToast(it.msg!!)
                }

                _updateUserResultCode.postValue(Event(it.code))
            }
        )
    }
}