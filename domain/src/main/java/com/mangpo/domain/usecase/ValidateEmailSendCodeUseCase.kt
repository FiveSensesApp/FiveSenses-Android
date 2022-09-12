package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.UserRepository

class ValidateEmailSendCodeUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, emailCode: Int): BaseResEntity<Nothing> {
        return repository.validateEmailSendCode(email, emailCode)
    }
}