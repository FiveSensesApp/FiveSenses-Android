package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.domain.model.createUser.CreateUserResEntity
import com.mangpo.domain.repository.AuthRepository

class CreateUserUseCase (private val repository: AuthRepository) {
    suspend operator fun invoke(createUserReqEntity: CreateUserReqEntity): BaseResEntity<CreateUserResEntity?> {
        return repository.createUser(createUserReqEntity)
    }
}