package com.mangpo.data.repository

import com.mangpo.data.datasource.PostRemoteDataSource
import com.mangpo.data.mapper.PostMapper
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getPosts.GetPostsResEntity
import com.mangpo.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(private val dataSource: PostRemoteDataSource): BaseRepository(), PostRepository {
    override suspend fun getPosts(
        userId: Int,
        page: Int,
        sort: String,
        createDate: String?,
        star: Int?,
        category: String?
    ): BaseResEntity<GetPostsResEntity?> {
        val response = dataSource.getPosts(userId, page, sort, createDate, star, category)
        return sendData(response) { PostMapper.mapperToGetPostsResEntity(response) }
    }
}