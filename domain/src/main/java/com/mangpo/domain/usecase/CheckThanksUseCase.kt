package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.domain.repository.BadgeRepository

class CheckThanksUseCase(private val repository: BadgeRepository) {
    suspend operator fun invoke(): BaseResEntity<GetUserBadgesByUserResEntity?> {
        return repository.checkThanks()
    }
}