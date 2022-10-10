package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.updateUser.UpdateUserReqEntity
import com.mangpo.domain.usecase.UpdateUserUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.util.SpfUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateNicknameViewModel @Inject constructor(private val updateUserUseCase: UpdateUserUseCase): BaseViewModel() {
    private val _updateNicknameResultCode: MutableLiveData<Int> = MutableLiveData()
    val updateNicknameResultCode: LiveData<Int> get() = _updateNicknameResultCode

    fun updateNickname(nickname: String) {
        val updateUserReqEntity: UpdateUserReqEntity = UpdateUserReqEntity(nickname=nickname, userId=SpfUtils.getIntEncryptedSpf("userId"))

        callApi(
            { updateUserUseCase.invoke(updateUserReqEntity) },
            {
                when (it.code) {
                    200 -> {
                        SpfUtils.writeSpf("nickname", nickname)
                        showToast("닉네임이 수정되었습니다.")
                    }
                    404 -> showToast(it.msg!!)
                }
                _updateNicknameResultCode.postValue(it.code)
            },
            true
        )
    }
}