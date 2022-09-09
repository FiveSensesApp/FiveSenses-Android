package com.mangpo.data.mapper

import com.mangpo.data.model.authorize.AuthorizeReqDTO
import com.mangpo.data.model.authorize.AuthorizeResDTO
import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.domain.model.authorize.AuthorizeReqEntity
import com.mangpo.domain.model.authorize.AuthorizeResEntity
import com.mangpo.domain.model.base.BaseResEntity

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
}