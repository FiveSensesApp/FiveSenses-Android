package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.UserRepository

class ValidateDuplicateUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String): BaseResEntity<Nothing> {
        return repository.validateDuplicate(email)
    }
}