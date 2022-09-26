package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.PostRepository

class FindCountByParamUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(userId: Int, category: String?, star: Int?, createdDate: String?): BaseResEntity<Int> {
        return repository.findCountByParam(userId, category, star, createdDate)
    }
}