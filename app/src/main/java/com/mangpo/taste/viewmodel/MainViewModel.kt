package com.mangpo.taste.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.usecase.GetUserInfoUseCase
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.base.Event
import com.mangpo.taste.util.SpfUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase): BaseViewModel() {
    private val _isTasteRecordShown: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isTasteRecordShown: LiveData<Event<Boolean>> get() = _isTasteRecordShown

    private val _callGetPostsFlag: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val callGetPostsFlag: LiveData<Event<Boolean>> get() = _callGetPostsFlag

    private val _callGetStatFlag: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val callGetStatFlag: LiveData<Event<Boolean>> get() = _callGetStatFlag

    private val _getUserInfoResult: MutableLiveData<Boolean> = MutableLiveData()
    val getUserInfoResult: LiveData<Boolean> get() = _getUserInfoResult

    private val _changeFragmentFlag: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val changeFragmentFlag: LiveData<Event<Boolean>> get() = _changeFragmentFlag

    val slogan: MutableLiveData<String> = MutableLiveData()

    private var isRecordComplete: Boolean = false

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
                        SpfUtils.writeSpf("badgeRepresent", it.data!!.badgeRepresent)

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
            true
        )
    }

    fun setIsTasteRecordShown(value: Boolean) {
        _isTasteRecordShown.postValue(Event(value))
    }

    fun setIsRecordComplete(value: Boolean) {
        isRecordComplete = value
    }

    fun getIsRecordComplete(): Boolean = this.isRecordComplete

    fun setCallGetPostsFlag(value: Boolean) {
        _callGetPostsFlag.postValue(Event(value))
    }

    fun setChangeFragmentFlag(changeFragmentFlag: Boolean) {
        _changeFragmentFlag.postValue(Event(changeFragmentFlag))
    }

    fun setCallGetStatFlag(value: Boolean) {
        _callGetStatFlag.postValue(Event(value))
    }
}