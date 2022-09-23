package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.updatePost.UpdatePostReqEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
import com.mangpo.domain.repository.PostRepository

class UpdatePostUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(updatePostReqEntity: UpdatePostReqEntity): BaseResEntity<UpdatePostResEntity?> {
        return repository.updatePost(updatePostReqEntity)
    }
}