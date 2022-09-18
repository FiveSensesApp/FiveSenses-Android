package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.createPost.CreatePostReqEntity
import com.mangpo.domain.repository.PostRepository

class CreatePostUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(createPostReqEntity: CreatePostReqEntity): BaseResEntity<Nothing> {
        return repository.createPost(createPostReqEntity)
    }
}