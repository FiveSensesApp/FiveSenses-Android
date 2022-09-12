package com.mangpo.data.datasource

import com.google.gson.Gson
import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.base.Meta
import retrofit2.HttpException
import java.io.IOException

abstract class BaseRemoteDataSource {
    suspend fun <T> callApi(apiCall: suspend () -> BaseResDTO<T>): BaseResDTO<T> {
        return try {
            apiCall.invoke()
        } catch (e: IOException) {  //네트워크 문제
            e.printStackTrace()
            BaseResDTO(null, Meta(600, e.toString()))
        } catch (e: HttpException) {
            Gson().fromJson<BaseResDTO<T>>(e.response()!!.errorBody()?.string(), BaseResDTO::class.java)
        } catch (e: Exception) {    //그 이외 문제
            BaseResDTO(null, Meta(700, e.toString()))
        }
    }
}