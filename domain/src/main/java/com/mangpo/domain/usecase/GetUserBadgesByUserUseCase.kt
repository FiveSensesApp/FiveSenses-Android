package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.domain.repository.BadgeRepository

class GetUserBadgesByUserUseCase(private val repository: BadgeRepository) {
    suspend operator fun invoke(userId: Int): BaseResEntity<List<GetUserBadgesByUserResEntity>?> {
        return repository.getUserBadgesByUser(userId)
    }
}