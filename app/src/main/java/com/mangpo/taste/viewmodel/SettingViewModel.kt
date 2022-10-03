package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.usecase.DeleteUserUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val deleteUserUseCase: DeleteUserUseCase): BaseViewModel() {
    private val _deleteUserResultCode: MutableLiveData<Int> = MutableLiveData()
    val deleteUserResultCode: LiveData<Int> get() = _deleteUserResultCode

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
}