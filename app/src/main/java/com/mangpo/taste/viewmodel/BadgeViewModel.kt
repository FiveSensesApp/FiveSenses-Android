package com.mangpo.taste.viewmodel

import com.mangpo.domain.usecase.CheckThanksUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BadgeViewModel @Inject constructor(private val checkThanksUseCase: CheckThanksUseCase): BaseViewModel() {
    fun checkThanks() {
        callApi(
            { checkThanksUseCase.invoke() },
            {},
            false
        )
    }
}