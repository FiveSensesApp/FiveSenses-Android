package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.usecase.ValidateDuplicateUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(private val validateDuplicateUseCase: ValidateDuplicateUseCase): BaseViewModel() {
    private val _validateDuplicateResult: MutableLiveData<Boolean> = MutableLiveData()
    val validateDuplicateResult: LiveData<Boolean> get() = _validateDuplicateResult

    fun validateDuplicate(email: String) {
        callApi(
            { validateDuplicateUseCase.invoke(email) },
            {
                when (it.code) {
                    200 -> _validateDuplicateResult.postValue(true)
                    400 -> {
                        _validateDuplicateResult.postValue(false)
                        showToast(it.msg!!)
                    }
                    else -> _validateDuplicateResult.postValue(false)
                }
            }
        )
    }
}