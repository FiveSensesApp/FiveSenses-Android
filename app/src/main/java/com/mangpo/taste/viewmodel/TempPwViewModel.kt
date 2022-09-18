package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.usecase.LostPasswordUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TempPwViewModel @Inject constructor(private val lostPasswordUseCase: LostPasswordUseCase): BaseViewModel() {
    private val _lostPasswordResult: MutableLiveData<Boolean> = MutableLiveData()
    val lostPasswordResult: LiveData<Boolean> get() = _lostPasswordResult

    fun lostPassword(email: String) {
        callApi(
            { lostPasswordUseCase.invoke(email) },
            {
                when (it.code) {
                    200 -> _lostPasswordResult.postValue(true)
                    404 -> {
                        showToast(it.msg!!)
                        _lostPasswordResult.postValue(false)
                    }
                    else -> _lostPasswordResult.postValue(false)
                }
            },
            true
        )
    }
}