package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.domain.model.getStat.GetStatResEntity

interface StatRemoteDataSource {
    suspend fun getStat(userId: Int): BaseResDTO<GetStatResEntity>
}