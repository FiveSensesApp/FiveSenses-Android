package com.mangpo.data.mapper

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserBadgesByUser.GetUserBadgesByUserResDTO
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity

object BadgeMapper {
    fun mapperToGetUserBadgesByUserResEntity(getUserBadgesByUserResDTO: BaseResDTO<GetUserBadgesByUserResDTO?>): BaseResEntity<GetUserBadgesByUserResEntity?> {
        return if (getUserBadgesByUserResDTO.data==null) {
            getUserBadgesByUserResDTO.run {
                BaseResEntity<GetUserBadgesByUserResEntity?>(meta.code, meta.msg, null)
            }
        } else {
            getUserBadgesByUserResDTO.run {
                BaseResEntity<GetUserBadgesByUserResEntity?>(meta.code, meta.msg, data!!.run {
                    GetUserBadgesByUserResEntity(description, id, imgUrl, isBefore, name, reqConditionShort, seqNum)
                })
            }
        }
    }

    fun mapperToGetUserBadgesByUserResEntities(getUserBadgesByUserResDTOs: BaseResDTO<List<GetUserBadgesByUserResDTO>?>): BaseResEntity<List<GetUserBadgesByUserResEntity>?> {
        if (getUserBadgesByUserResDTOs.data==null) {
            return getUserBadgesByUserResDTOs.run {
                BaseResEntity<List<GetUserBadgesByUserResEntity>?>(meta.code, meta.msg, null)
            }
        } else {
            val getUserBadgesByUserResEntities: MutableList<GetUserBadgesByUserResEntity> = mutableListOf()

            for (getUserBadgesByUserResDTO in getUserBadgesByUserResDTOs.data) {
                getUserBadgesByUserResEntities.add(getUserBadgesByUserResDTO!!.run {
                    GetUserBadgesByUserResEntity(description, id, imgUrl, isBefore, name, reqConditionShort, seqNum)
                })
            }

            return getUserBadgesByUserResDTOs.run {
                BaseResEntity(meta.code, meta.msg, getUserBadgesByUserResEntities)
            }
        }
    }
}