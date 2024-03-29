package com.mangpo.data.mapper

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.changePassword.ChangePasswordReqDTO
import com.mangpo.data.model.getUserInfo.GetUserInfoResDTO
import com.mangpo.data.model.updateUser.UpdateUserReqDTO
import com.mangpo.data.util.MapperUtils.calDate
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.changePassword.ChangePasswordReqEntity
import com.mangpo.domain.model.getUserInfo.GetUserInfoResEntity
import com.mangpo.domain.model.updateUser.UpdateUserReqEntity

object UserMapper {
    fun mapperToGetUserInfoEntity(getUserInfoResDTO: BaseResDTO<GetUserInfoResDTO?>): BaseResEntity<GetUserInfoResEntity?> {
        return getUserInfoResDTO.run {
            if (data==null)
                BaseResEntity<GetUserInfoResEntity?>(meta.code, meta.msg, null)
            else
                BaseResEntity<GetUserInfoResEntity?>(meta.code, meta.msg, data.run {
                    GetUserInfoResEntity(id, nickname, email, calDate(createdDate), isAlarmOn, alarmDate, badgeRepresent)
                })
        }
    }

    fun mapperToUpdateUserReqDTO(updateUserReqEntity: UpdateUserReqEntity): UpdateUserReqDTO {
        return updateUserReqEntity.run {
            UpdateUserReqDTO(alarmDate, isAlarmOn, nickname, badgeRepresent, userId)
        }
    }

    fun mapperToChangePasswordReqDTO(changePasswordReqEntity: ChangePasswordReqEntity): ChangePasswordReqDTO {
        return changePasswordReqEntity.run {
            ChangePasswordReqDTO(newPw, ogPw)
        }
    }
}