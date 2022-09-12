package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.getUserInfo.GetUserInfoResEntity
import com.mangpo.domain.usecase.GetUserInfoUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.util.SpfUtils.writeEncryptedSpf
import com.mangpo.taste.util.SpfUtils.writeSpf
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase): BaseViewModel() {
    private val _randomSloganIdx: MutableLiveData<Int> = MutableLiveData()
    val randomSloganIdx: LiveData<Int> = _randomSloganIdx

    private val _feedType: MutableLiveData<String> = MutableLiveData()
    val feedType: LiveData<String> = _feedType

    private val _user: MutableLiveData<GetUserInfoResEntity> = MutableLiveData()
    val user: LiveData<GetUserInfoResEntity> get() = _user

    private val _getUserInfoResult: MutableLiveData<Boolean> = MutableLiveData()
    val getUserInfoResult: LiveData<Boolean> get() = _getUserInfoResult

    fun setRandomSloganIdx() {
        _randomSloganIdx.value = Random().nextInt(6)
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
                        writeSpf("nickname", it.data!!.nickname)
                        writeEncryptedSpf("email", it.data!!.email)
                        writeSpf("startDayCnt", it.data!!.startDayCnt)
                        writeSpf("isAlarmOn", it.data!!.isAlarmOn)
                        writeSpf("alarmTime", it.data!!.alarmTime)
                    }
                    404 -> {
                        _getUserInfoResult.postValue(false)
                        showToast(it.msg!!)
                    }
                }
            },
            false
        )
    }
}