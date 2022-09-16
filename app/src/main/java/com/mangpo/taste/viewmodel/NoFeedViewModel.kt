package com.mangpo.taste.viewmodel

import com.mangpo.domain.usecase.GetUserInfoUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoFeedViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase): BaseViewModel() {
}