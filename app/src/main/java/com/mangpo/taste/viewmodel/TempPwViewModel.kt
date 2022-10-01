package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.usecase.LostPasswordUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TempPwViewModel @Inject constructor(private val lostPasswordUseCase: LostPasswordUseCase): BaseViewModel() {
    private val _lostPasswordResCode: MutableLiveData<Int> = MutableLiveData()
    val lostPasswordResCode: LiveData<Int> get() = _lostPasswordResCode

    fun lostPassword(email: String) {
        callApi(
            { lostPasswordUseCase.invoke(email) },
            { _lostPasswordResCode.postValue(it.code) },
            true
        )
    }
}