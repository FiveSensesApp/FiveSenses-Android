package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.KakaoBookEntiity
import com.mangpo.domain.usecase.GetBooksByNameUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getBooksByNameUseCase: GetBooksByNameUseCase): BaseViewModel() {
    private val _books: MutableLiveData<List<KakaoBookEntiity>> = MutableLiveData()
    val books: LiveData<List<KakaoBookEntiity>> = _books

    fun getBooksByName(name: String) {
        getApiResult({ getBooksByNameUseCase.invoke(name) }, { _books.value = it }, showLoading = true, title = "책 검색하기")
    }
}