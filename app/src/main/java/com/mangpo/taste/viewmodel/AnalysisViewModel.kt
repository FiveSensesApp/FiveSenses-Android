package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.getStat.GetStatResEntity
import com.mangpo.domain.usecase.GetStatUseCase
import com.mangpo.taste.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(private val getStatUseCase: GetStatUseCase): BaseViewModel() {
    private val _getStatResEntity: MutableLiveData<GetStatResEntity> = MutableLiveData()
    val getStatResEntity: LiveData<GetStatResEntity> get() = _getStatResEntity

    fun getStat(userId: Int) {
        callApi(
            { getStatUseCase.invoke(userId) },
            {
                _getStatResEntity.postValue(it.data)
            },
            true
        )
    }
}