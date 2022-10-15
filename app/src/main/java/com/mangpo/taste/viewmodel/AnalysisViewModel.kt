package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.getStat.GetStatResEntity
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.domain.usecase.GetStatUseCase
import com.mangpo.domain.usecase.GetUserBadgesByUserUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.util.SpfUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(private val getStatUseCase: GetStatUseCase, private val getUserBadgesByUserUseCase: GetUserBadgesByUserUseCase): BaseViewModel() {
    private val _getStatResEntity: MutableLiveData<GetStatResEntity> = MutableLiveData()
    val getStatResEntity: LiveData<GetStatResEntity> get() = _getStatResEntity

    private var badges: List<GetUserBadgesByUserResEntity> = listOf()
    val representativeBadge: MutableLiveData<GetUserBadgesByUserResEntity?> = MutableLiveData()
    val acquiredBadges: MutableLiveData<List<GetUserBadgesByUserResEntity>> = MutableLiveData()

    fun getStat(userId: Int) {
        callApi(
            { getStatUseCase.invoke(userId) },
            {
                _getStatResEntity.postValue(it.data)
            },
            true
        )
    }

    fun getBadges(userId: Int) {
        callApi(
            { getUserBadgesByUserUseCase.invoke(userId) },
            {
                if (it.data!=null) {
                    val names: List<String> = listOf("첫 감각의 설렘", "공유하는 기쁨", "투머치토커", "프로미각러")
                    badges = it.data!!
                    representativeBadge.postValue(it.data?.find { it.id== SpfUtils.getStrSpf("badgeRepresent") })
                    acquiredBadges.postValue(it.data?.filter { names.contains(it.name) }?.sortedBy { it.seqNum })
                }
            },
            true
        )
    }

    fun getBadges(): List<GetUserBadgesByUserResEntity> = badges
}