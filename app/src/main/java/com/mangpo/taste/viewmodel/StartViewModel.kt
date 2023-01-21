package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.auth0.android.jwt.JWT
import com.mangpo.domain.model.authorizeNew.AuthorizeNewReqEntity
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.domain.usecase.AuthorizeNewUseCase
import com.mangpo.domain.usecase.CreateUserUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.util.SpfUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(private val createUserUseCase: CreateUserUseCase, private val authorizeNewUseCase: AuthorizeNewUseCase): BaseViewModel() {
    private val _createUserResult: MutableLiveData<Boolean> = MutableLiveData()
    val createUserResult: LiveData<Boolean> get() = _createUserResult

    private val _loginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    fun createUser(createUserReqEntity: CreateUserReqEntity) {
        callApi(
            { createUserUseCase.invoke(createUserReqEntity) },
            {
                when (it.code) {
                    201 -> _createUserResult.postValue(true)
                    400 -> {
                        showToast(it.msg!!)
                        _createUserResult.postValue(false)
                    }
                    else -> _createUserResult.postValue(false)
                }
            },
            true
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