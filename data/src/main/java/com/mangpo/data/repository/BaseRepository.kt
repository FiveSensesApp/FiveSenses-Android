package com.mangpo.data.repository

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.domain.model.NetworkResult
import com.mangpo.domain.model.base.BaseResEntity

abstract class BaseRepository {
    private fun <T> NetworkResult<T>.toModel() : T =
        (this as NetworkResult.Success).data

    private fun <R> changeNetworkData(replaceData: R): NetworkResult<R> {
        return NetworkResult.Success(replaceData)
    }

    suspend fun <T,R> NetworkResult<T>.mapNetworkResult(getData : suspend (T) -> R) : NetworkResult<R>{
        return changeNetworkData(getData(toModel()))
    }

    suspend fun <DTO, Any> sendData(data: BaseResDTO<DTO>, mapper: (BaseResDTO<DTO>) -> BaseResEntity<Any>): BaseResEntity<Any> {
        return if (data.meta.code<300) {    //성공
            mapper.invoke(data)
        } else {    //실패
            BaseResEntity(data.meta.code, data.meta.msg, null)
        }
    }
}