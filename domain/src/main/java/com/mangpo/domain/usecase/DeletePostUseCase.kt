package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.repository.PostRepository

class DeletePostUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(postId: Int): BaseResEntity<Nothing> {
        return repository.deletePost(postId)
    }
}