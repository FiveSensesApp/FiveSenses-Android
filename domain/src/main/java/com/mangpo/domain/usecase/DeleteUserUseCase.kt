package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.AdminRepository

class DeleteUserUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(userId: Int): BaseResEntity<Nothing> {
        return repository.deleteUser(userId)
    }
}