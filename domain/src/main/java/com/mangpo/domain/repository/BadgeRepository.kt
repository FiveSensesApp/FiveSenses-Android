package com.mangpo.domain.repository

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity

interface BadgeRepository {
    suspend fun getUserBadgesByUser(userId: Int): BaseResEntity<List<GetUserBadgesByUserResEntity>?>
}