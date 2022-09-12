package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getPosts.GetPostsResDTO
import com.mangpo.data.service.PostService
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor(private val service: PostService): BaseRemoteDataSource(), PostRemoteDataSource {
    override suspend fun getPosts(
        userId: Int,
        page: Int,
        sort: String,
        createDate: String?,
        star: Int?,
        category: String?
    ): BaseResDTO<GetPostsResDTO?> {
        return callApi { service.getPosts(userId, page, sort, createDate, star, category) }
    }
}