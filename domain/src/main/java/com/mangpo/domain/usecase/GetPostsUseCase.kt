package com.mangpo.domain.usecase

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getPosts.GetPostsResEntity
import com.mangpo.domain.repository.PostRepository

class GetPostsUseCase (private val repository: PostRepository) {
    suspend operator fun invoke(userId: Int, page: Int, sort: String, createDate: String?, star: Int?, category: String?): BaseResEntity<GetPostsResEntity?> {
        return repository.getPosts(userId, page, sort, createDate, star, category)
    }
}