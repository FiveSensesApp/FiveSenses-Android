package com.mangpo.domain.model.base

data class BaseResEntity<Any>(
    val code: Int,
    val msg: String?,
    val data: Any?
)