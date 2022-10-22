package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getUserBadgesByUser.GetUserBadgesByUserResDTO
import com.mangpo.data.service.BadgeService
import javax.inject.Inject

class BadgeRemoteDataSourceImpl @Inject constructor(private val service: BadgeService): BaseRemoteDataSource(), BadgeRemoteDataSource {
    override suspend fun getUserBadgesByUser(userId: Int): BaseResDTO<List<GetUserBadgesByUserResDTO>?> {
        return callApi {
            service.checkUpdates()
            service.getUserBadgesByUser(userId) }
    }
}