package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getPresentPostsBetween.GetPresentPostsBetweenResEntity
import com.mangpo.domain.repository.PostRepository

class GetPresentPostsBetweenUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(startDate: String, endDate: String): BaseResEntity<List<GetPresentPostsBetweenResEntity>> {
        return repository.getPresentPostsBetween(startDate, endDate)
    }
}