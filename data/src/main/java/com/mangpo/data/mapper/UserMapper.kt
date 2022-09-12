package com.mangpo.data.mapper

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserInfo.GetUserInfoResDTO
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getUserInfo.GetUserInfoResEntity
import java.text.SimpleDateFormat
import java.util.*

object UserMapper {
    fun mapperToGetUserInfoEntity(getUserInfoResDTO: BaseResDTO<GetUserInfoResDTO?>): BaseResEntity<GetUserInfoResEntity?> {
        return getUserInfoResDTO.run {
            if (data==null)
                BaseResEntity<GetUserInfoResEntity?>(meta.code, meta.msg, null)
            else
                BaseResEntity<GetUserInfoResEntity?>(meta.code, meta.msg, data.run {
                    GetUserInfoResEntity(id, nickname, email, calDate(createdDate), isAlarmOn, alarmDate)
                })
        }
    }

    private fun calDate(createdDate: String): Int = ((Calendar.getInstance().time.time - SimpleDateFormat("yyyy-MM-dd").parse(createdDate).time) / (24 * 60 * 60 * 1000)).toInt()
}