package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getUserInfo.GetUserInfoResEntity
import com.mangpo.domain.repository.UserRepository

class GetUserInfoUseCase (private val repository: UserRepository) {
    suspend operator fun invoke(userIdx: Int): BaseResEntity<GetUserInfoResEntity?> {
        return repository.getUserInfo(userIdx)
    }
}