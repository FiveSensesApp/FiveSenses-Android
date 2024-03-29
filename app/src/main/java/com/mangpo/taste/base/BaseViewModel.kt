package com.mangpo.taste.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.domain.model.base.BaseResEntity
import kotlinx.coroutines.*

open class BaseViewModel(): ViewModel() {
    lateinit var job: Job

    private val _isLoading by lazy { MutableLiveData(false) }
    val isLoading: LiveData<Boolean> by lazy { _isLoading }

    private val _toast by lazy { MutableLiveData<Event<String>>() }
    val toast: LiveData<Event<String>> by lazy { _toast }

    private val _tokenExpired by lazy { MutableLiveData<Boolean>(false) }
    val tokenExpired: LiveData<Boolean> by lazy {_tokenExpired }

    private fun handleLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
    }

    fun showToast(message: String) {
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
                    401 -> {
                        if (response.msg=="유효한 액세스 토큰이 없습니다.") {
                            _tokenExpired.postValue(true)
                        } else {
                            callback(response)
                        }
                    }
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

    private suspend fun startLoading(block: suspend () -> Unit) {
        handleLoading(true)
        block()
        handleLoading(false)
    }
}