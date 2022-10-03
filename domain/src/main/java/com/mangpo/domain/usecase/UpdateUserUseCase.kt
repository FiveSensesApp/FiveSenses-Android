package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.updateUser.UpdateUserReqEntity
import com.mangpo.domain.repository.UserRepository

class UpdateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(updateUserReqEntity: UpdateUserReqEntity): BaseResEntity<Nothing> {
        return repository.updateUser(updateUserReqEntity)
    }
}