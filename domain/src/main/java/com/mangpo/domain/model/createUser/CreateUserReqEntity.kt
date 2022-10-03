package com.mangpo.domain.model.createUser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateUserReqEntity(
    var alarmDate: String = "",
    var email: String = "",
    var isAlarmOn: Boolean = false,
    var nickname: String = "",
    var password: String = "",
    var isMarketingAllowed: Boolean = false
) : Parcelable