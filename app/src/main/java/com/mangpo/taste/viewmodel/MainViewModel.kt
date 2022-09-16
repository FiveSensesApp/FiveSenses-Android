package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.getUserInfo.GetUserInfoResEntity
import com.mangpo.domain.usecase.GetUserInfoUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.util.SpfUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase): BaseViewModel() {
    private val _feedType: MutableLiveData<String> = MutableLiveData()
    val feedType: LiveData<String> = _feedType

    private val _user: MutableLiveData<GetUserInfoResEntity> = MutableLiveData()
    val user: LiveData<GetUserInfoResEntity> get() = _user

    private val _getUserInfoResult: MutableLiveData<Boolean> = MutableLiveData()
    val getUserInfoResult: LiveData<Boolean> get() = _getUserInfoResult

    val slogan: MutableLiveData<String> = MutableLiveData()

    fun setRandomSloganIdx() {
        val slogans: List<String> = listOf(
            "감각을 통해 취향을 쌓는 즐거움!",
            "당신은 무엇을 감각할 때 행복한가요?",
            "내가 보고 듣고 느끼는 나만의 취향",
            "감각하며 수집하는 일상 속 취향",
            "오감으로 찾는 나만의 취향",
            "지극히 나다운 취향수집함"
        )

        slogan.postValue(slogans[Random().nextInt(6)])
    }

    fun setFeedType(feedType: String) {
        _feedType.value = feedType
    }

    fun getUserInfo(userIdx: Int) {
        callApi(
            { getUserInfoUseCase.invoke(userIdx) },
            {
                when(it.code) {
                    200 -> {
                        SpfUtils.writeSpf("nickname", it.data!!.nickname)
                        SpfUtils.writeEncryptedSpf("email", it.data!!.email)
                        SpfUtils.writeSpf("startDayCnt", it.data!!.startDayCnt)
                        SpfUtils.writeSpf("isAlarmOn", it.data!!.isAlarmOn)
                        SpfUtils.writeSpf("alarmTime", it.data!!.alarmTime)

                        _getUserInfoResult.postValue(true)
                    }
                    404 -> {
                        showToast(it.msg!!)

                        _getUserInfoResult.postValue(false)
                    }
                    else -> {
                        _getUserInfoResult.postValue(false)
                    }
                }
            },
            false
        )
    }
}