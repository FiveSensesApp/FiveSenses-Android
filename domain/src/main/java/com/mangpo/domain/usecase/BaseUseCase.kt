package com.mangpo.domain.usecase

import com.mangpo.domain.model.NetworkResult
import java.io.IOException

abstract class BaseUseCase {
    suspend fun <T> networkHandling(block : suspend () -> T) : NetworkResult<T> {
        return try {
            NetworkResult.Success(block())
        } catch (e : Exception){
            when(e){
                is IOException -> NetworkResult.Fail("네트워크 상태를 확인해주세요.")
                else -> NetworkResult.Fail("알 수 없는 오류입니다.")
                /*is JwtRefreshException -> {
                    JwtRefresh.isJwtRefresh = true
                    Log.d("Network Exception","JwtRefreshException")
                    networkHandling { block() }
                }
                is TokenExpireException -> {
                    Log.d("Network Exception","TokenExpireException")
                    NetworkResult.TokenExpired
                }
                is ServerFailException -> {
                    NetworkResult.Fail(e.message?:"")
                }
                else -> {
                    Log.d("Network Exception","UnknownException")
                    NetworkResult.Exception(e)
                }*/
            }
        }
    }
}

fun <T> NetworkResult<T>.toModel() : T =
    (this as NetworkResult.Success).data

suspend fun <T,R> NetworkResult<T>.map(getData : suspend (T) -> R) : R{
    val data = toModel()
    return getData(data)
}