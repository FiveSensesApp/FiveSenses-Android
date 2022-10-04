package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getStat.GetStatResDTO

interface StatRemoteDataSource {
    suspend fun getStat(userId: Int): BaseResDTO<GetStatResDTO>
}