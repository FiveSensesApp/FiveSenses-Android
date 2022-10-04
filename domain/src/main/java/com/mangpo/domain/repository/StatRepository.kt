package com.mangpo.domain.repository

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getStat.GetStatResEntity

interface StatRepository {
    suspend fun getStat(userId: Int): BaseResEntity<GetStatResEntity>
}