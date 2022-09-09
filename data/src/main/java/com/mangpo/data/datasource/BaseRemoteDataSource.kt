package com.mangpo.data.datasource

import android.util.Log
import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.domain.model.NetworkResult
import com.mangpo.data.model.base.Meta
import java.io.IOException

abstract class BaseRemoteDataSource {
    /*suspend fun <T> toNetworkResult(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            NetworkResult.Success(apiCall.invoke().data)
        } catch (e: IOException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }*/

    suspend fun <T> toNetworkResult(apiCall: suspend () -> T): NetworkResult<T> {
        return try {
            NetworkResult.Success(apiCall.invoke())
        } catch (e: IOException) {
            Log.d("BaseRemoteDataSource", "네트워크 연결 해제")
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun <T> callApi(apiCall: suspend () -> BaseResDTO<T>): BaseResDTO<T> {
        return try {
            apiCall.invoke()
        } catch (e: IOException) {  //네트워크 문제
            e.printStackTrace()
            BaseResDTO(null, Meta(600, e.toString()))
        } catch (e: Exception) {    //그 이외 문제
            BaseResDTO(null, Meta(700, e.toString()))
        }
    }
}