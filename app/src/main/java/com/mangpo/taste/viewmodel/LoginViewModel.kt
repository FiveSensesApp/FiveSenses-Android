package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.auth0.android.jwt.JWT
import com.mangpo.domain.model.authorize.AuthorizeReqEntity
import com.mangpo.domain.model.authorizeNew.AuthorizeNewReqEntity
import com.mangpo.domain.usecase.AuthorizeNewUseCase
import com.mangpo.domain.usecase.AuthorizeUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.util.SpfUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authorizeUseCase: AuthorizeUseCase, private val authorizeNewUseCase: AuthorizeNewUseCase): BaseViewModel() {
    private val _loginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    fun authorize(authorizeReqEntity: AuthorizeReqEntity) {
        callApi(
            apiResult = { authorizeUseCase.invoke(authorizeReqEntity) },
            callback = {
                when (it.code) {
                    200 -> {
                        val jwt = JWT(it.data!!.token)
                        SpfUtils.writeEncryptedSpf("userId", jwt.getClaim("sub").asString()!!.toInt())
                        SpfUtils.writeEncryptedSpf("jwt", it.data!!.token)

                        _loginSuccess.postValue(true)
                    }
                    401 -> _loginSuccess.postValue(false)
                }
            },
            showLoading = true,
        )
    }

    fun authorizeNew(authorizeNewReqEntity: AuthorizeNewReqEntity) {
        callApi(
            apiResult = { authorizeNewUseCase.invoke(authorizeNewReqEntity) },
            callback = {
                when (it.code) {
                    200 -> {
                        val jwt = JWT(it.data!!.accessToken)
                        SpfUtils.writeEncryptedSpf("userId", jwt.getClaim("sub").asString()!!.toInt())
                        SpfUtils.writeEncryptedSpf("jwt", it.data!!.accessToken)
                        SpfUtils.writeEncryptedSpf("refreshToken", it.data!!.refreshToken)

                        _loginSuccess.postValue(true)
                    }
                    401 -> _loginSuccess.postValue(false)
                }
            },
            showLoading = true,
        )
    }
}