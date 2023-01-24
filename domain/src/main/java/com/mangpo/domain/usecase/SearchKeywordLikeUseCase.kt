package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.repository.PostRepository

class SearchKeywordLikeUseCase (private val repository: PostRepository) {
    suspend operator fun invoke(query: String): BaseResEntity<List<ContentEntity>> {
        return repository.searchKeywordLike(query)
    }
}