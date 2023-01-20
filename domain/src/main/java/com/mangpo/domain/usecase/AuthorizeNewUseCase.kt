package com.mangpo.domain.usecase

import com.mangpo.domain.model.authorizeNew.AuthorizeNewReqEntity
import com.mangpo.domain.model.authorizeNew.AuthorizeNewResEntity
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.AuthRepository

class AuthorizeNewUseCase (private val repository: AuthRepository) {
    suspend operator fun invoke(authorizeNewReqEntity: AuthorizeNewReqEntity): BaseResEntity<AuthorizeNewResEntity?> {
        return repository.authorizeNew(authorizeNewReqEntity)
    }
}