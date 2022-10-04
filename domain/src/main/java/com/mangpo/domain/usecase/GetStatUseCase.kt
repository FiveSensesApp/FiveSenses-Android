package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getStat.GetStatResEntity
import com.mangpo.domain.repository.StatRepository

class GetStatUseCase (private val repository: StatRepository) {
    suspend operator fun invoke(userId: Int): BaseResEntity<GetStatResEntity> {
        return repository.getStat(userId)
    }
}