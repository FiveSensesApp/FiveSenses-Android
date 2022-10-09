package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserBadgesByUser.GetUserBadgesByUserResDTO

interface BadgeRemoteDataSource {
    suspend fun getUserBadgesByUser(userId: Int): BaseResDTO<List<GetUserBadgesByUserResDTO>?>
}