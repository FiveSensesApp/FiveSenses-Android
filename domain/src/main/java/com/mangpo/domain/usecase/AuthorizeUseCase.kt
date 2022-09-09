package com.mangpo.domain.usecase

import com.mangpo.domain.model.authorize.AuthorizeReqEntity
import com.mangpo.domain.model.authorize.AuthorizeResEntity
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.AuthRepository

class AuthorizeUseCase (private val repository: AuthRepository) {
    suspend operator fun invoke(authorizeReqEntity: AuthorizeReqEntity): BaseResEntity<AuthorizeResEntity> {
        return repository.authorize(authorizeReqEntity)
    }
}