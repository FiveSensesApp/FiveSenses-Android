package com.mangpo.domain.model

sealed class NetworkResult<out DATA> {
    data class Success<DATA>(var data : DATA) : NetworkResult<DATA>()
    data class Fail(val message : String) : NetworkResult<Nothing>()
    object TokenExpired : NetworkResult<Nothing>()
    data class Exception(val exception: java.lang.Exception) : NetworkResult<Nothing>()
}
