package com.mangpo.data.datasource

import android.util.Log
import com.mangpo.domain.model.NetworkResult
import com.mangpo.data.model.Response
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
}