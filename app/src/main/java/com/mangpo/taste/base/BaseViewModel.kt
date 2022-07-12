package com.mangpo.taste.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.domain.model.NetworkResult
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