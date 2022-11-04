package com.mangpo.data.repository

import com.mangpo.data.datasource.BadgeRemoteDataSource
import com.mangpo.data.mapper.BadgeMapper
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.domain.repository.BadgeRepository
import javax.inject.Inject

class BadgeRepositoryImpl @Inject constructor(private val badgeRemoteDataSource: BadgeRemoteDataSource): BaseRepository(), BadgeRepository {
    override suspend fun checkThanks(): BaseResEntity<GetUserBadgesByUserResEntity?> {
        val response = badgeRemoteDataSource.checkThanks()
        return sendData(response) { BadgeMapper.mapperToGetUserBadgesByUserResEntity(response) }
    }

    override suspend fun getUserBadgesByUser(userId: Int): BaseResEntity<List<GetUserBadgesByUserResEntity>?> {
        val response = badgeRemoteDataSource.getUserBadgesByUser(userId)
        return sendData(response) { BadgeMapper.mapperToGetUserBadgesByUserResEntities(response) }
    }
}