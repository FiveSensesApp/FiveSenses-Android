package com.mangpo.taste.viewmodel

import android.util.Log
import com.mangpo.domain.usecase.CheckThanksUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BadgeViewModel @Inject constructor(private val checkThanksUseCase: CheckThanksUseCase): BaseViewModel() {
    fun checkThanks() {
        callApi(
            { checkThanksUseCase.invoke() },
            {
                Log.d("BannerViewModel", "checkThanks -> $it")
            },
            false
        )
    }
}