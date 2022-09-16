package com.mangpo.data.mapper

import com.mangpo.data.model.authorize.AuthorizeReqDTO
import com.mangpo.data.model.authorize.AuthorizeResDTO
import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createUser.CreateUserReqDTO
import com.mangpo.data.model.createUser.CreateUserResDTO
import com.mangpo.domain.model.authorize.AuthorizeReqEntity
import com.mangpo.domain.model.authorize.AuthorizeResEntity
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.domain.model.createUser.CreateUserResEntity

object AuthMapper {
    fun mapperToAuthorizeReqDTO(authorizeReqEntity: AuthorizeReqEntity): AuthorizeReqDTO {
        return authorizeReqEntity.run {
            AuthorizeReqDTO(email, password)    //나중에 여기서 password 암호화하기
        }
    }

    fun mapperToAuthorizeResEntity(authorizeResDTO: BaseResDTO<AuthorizeResDTO>): BaseResEntity<AuthorizeResEntity> {
        return authorizeResDTO.run {
            BaseResEntity(meta.code, meta.msg, AuthorizeResEntity(data!!.token))
        }
    }

    fun mapperToCreateUserReqDTO(createUserReqEntity: CreateUserReqEntity): CreateUserReqDTO {
        return createUserReqEntity.run {
            CreateUserReqDTO(alarmDate, email, isAlarmOn, nickname, password)
        }
    }

    fun mapperToCreateUserResEntity(createUserResDTO: BaseResDTO<CreateUserResDTO?>): BaseResEntity<CreateUserResEntity?> {
        return if (createUserResDTO.data==null) {
            createUserResDTO.run {
                BaseResEntity(meta.code, meta.msg, null)
            }
        } else {
            createUserResDTO.run {
                BaseResEntity(meta.code, meta.msg, data!!.run {
                    CreateUserResEntity(alarmDate, createdDate.split("T")[0], email, id, isAlarmOn, nickname)
                })
            }
        }
    }
}