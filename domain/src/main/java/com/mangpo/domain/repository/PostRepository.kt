package com.mangpo.domain.repository

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getPosts.GetPostsResEntity

interface PostRepository {
    suspend fun getPosts(userId: Int, page: Int, sort: String, createDate: String?, star: Int?, category: String?): BaseResEntity<GetPostsResEntity?>
}