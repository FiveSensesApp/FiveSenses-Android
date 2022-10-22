package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.changePassword.ChangePasswordReqEntity
import com.mangpo.domain.repository.UserRepository

class ChangePasswordUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(changePasswordReqEntity: ChangePasswordReqEntity): BaseResEntity<Nothing> {
        return repository.changePassword(changePasswordReqEntity)
    }
}