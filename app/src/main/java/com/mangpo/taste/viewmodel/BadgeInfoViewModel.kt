package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.domain.model.updateUser.UpdateUserReqEntity
import com.mangpo.domain.usecase.UpdateUserUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.base.Event
import com.mangpo.taste.util.SpfUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BadgeInfoViewModel @Inject constructor(private val updateUserUseCase: UpdateUserUseCase): BaseViewModel() {
    private val _updateBadgeResult: MutableLiveData<Event<Int>> = MutableLiveData()
    val updateBadgeResult: LiveData<Event<Int>> get() = _updateBadgeResult

    fun updateBadge(badge: GetUserBadgesByUserResEntity) {
        callApi(
            {
                val updateUserReqEntity: UpdateUserReqEntity = UpdateUserReqEntity(badgeRepresent = badge.id, userId = SpfUtils.getIntEncryptedSpf("userId"))
                updateUserUseCase.invoke(updateUserReqEntity)
            },
            {
                when (it.code) {
                    200 -> { SpfUtils.writeSpf("badgeRepresent", badge.id) }
                    else -> { showToast("대표 뱃지 설정 중 문제가 발생했습니다.") }
                }
                _updateBadgeResult.postValue(Event(it.code))
            },
            true
        )
    }
}