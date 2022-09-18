package com.mangpo.data.mapper

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.domain.model.base.BaseResEntity

object BaseMapper {
    fun mapperToBaseResEntity(baseResDTO: BaseResDTO<Nothing>): BaseResEntity<Nothing> {
        return baseResDTO.run {
            BaseResEntity(meta.code, meta.msg, data)
        }
    }

    fun <Any> mapperToBaseResEntityVerAny(baseResDTO: BaseResDTO<Any>): BaseResEntity<Nothing> {
        return baseResDTO.run {
            BaseResEntity(meta.code, meta.msg, null)
        }
    }
}