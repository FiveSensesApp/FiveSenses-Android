package com.mangpo.taste.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.domain.model.NetworkResult
import com.mangpo.domain.model.base.BaseResEntity
import kotlinx.coroutines.*

open class BaseViewModel(): ViewModel() {
    lateinit var job: Job

    private val _isLoading by lazy { MutableLiveData(false) }
    val isLoading: LiveData<Boolean> by lazy { _isLoading }

    private val _toast by lazy { MutableLiveData<Event<String>>() }
    val toast: LiveData<Event<String>> by lazy { _toast }

    private fun handleLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
    }

    private fun showToast(message: String) {
        _toast.postValue(Event(message))
    }

    fun <T> callApi(
        apiResult: suspend () -> BaseResEntity<T>,
        callback: (BaseResEntity<T>) -> Unit,
        showLoading: Boolean = true,
    ) {
        job = viewModelScope.launch {
            val block = suspend {
                val response = apiResult.invoke()

                when (response.code) {
                    500 -> _toast.postValue(Event<String>("서버에 문제가 발생했습니다. 잠시만 기다려주세요."))
                    600 -> _toast.postValue(Event<String>("네트워크를 확인해주세요."))
                    700 -> _toast.postValue(Event<String>("알 수 없는 에러가 발생했습니다."))
                    else -> callback(response)
                }
            }

            if(showLoading)
                startLoading(block)
            else
                block()
        }
    }

    fun <T> getApiResult(
        apiResult: suspend () -> NetworkResult<T>,
        success: (T) -> Unit,
        showLoading: Boolean = true,
        title: String
    ) {
        job = viewModelScope.launch {
            val block = suspend {
                when (val networkResult = apiResult()) {
                    is NetworkResult.Success -> success(networkResult.data)
                    is NetworkResult.Fail -> showToast(networkResult.message)
                }
            }

            if(showLoading)
                startLoading(block)
            else
                block()
        }
    }

    private suspend fun startLoading(block: suspend () -> Unit) {
        handleLoading(true)
        block()
        handleLoading(false)
    }

    fun cancel() {
        job.cancel()

        if (_isLoading.value==true)
            handleLoading(false)
    }
}