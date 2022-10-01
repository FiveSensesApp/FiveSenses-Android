package com.mangpo.data.repository

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.domain.model.base.BaseResEntity

abstract class BaseRepository {
    fun <DTO, Any> sendData(data: BaseResDTO<DTO>, mapper: (BaseResDTO<DTO>) -> BaseResEntity<Any>): BaseResEntity<Any> {
        return if (data.meta.code<300) {    //성공
            mapper.invoke(data)
        } else {    //실패
            BaseResEntity(data.meta.code, data.meta.msg, null)
        }
    }
}