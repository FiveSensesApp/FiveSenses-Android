package com.mangpo.data.repository

import com.mangpo.domain.model.NetworkResult

abstract class BaseRepository {
    private fun <T> NetworkResult<T>.toModel() : T =
        (this as NetworkResult.Success).data

    private fun <R> changeNetworkData(replaceData: R): NetworkResult<R> {
        return NetworkResult.Success(replaceData)
    }

    suspend fun <T,R> NetworkResult<T>.mapNetworkResult(getData : suspend (T) -> R) : NetworkResult<R>{
        return changeNetworkData(getData(toModel()))
    }
}