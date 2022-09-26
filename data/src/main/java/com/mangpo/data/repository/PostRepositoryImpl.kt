package com.mangpo.data.repository

import com.mangpo.data.datasource.PostRemoteDataSource
import com.mangpo.data.mapper.BaseMapper
import com.mangpo.data.mapper.PostMapper
import com.mangpo.data.model.createPost.CreatePostReqDTO
import com.mangpo.data.model.updatePost.UpdatePostReqDTO
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.createPost.CreatePostReqEntity
import com.mangpo.domain.model.getPosts.GetPostsResEntity
import com.mangpo.domain.model.updatePost.UpdatePostReqEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
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

    override suspend fun findCountByParam(
        userId: Int,
        category: String?,
        star: Int?,
        createdDate: String?
    ): BaseResEntity<Int> {
        val response = dataSource.findCountByParam(userId, category, star, createdDate)

        return sendData(response) {  BaseMapper.mapperToBaseResEntityVerGeneric(response) }
    }

    override suspend fun createPost(createPostReqEntity: CreatePostReqEntity): BaseResEntity<Nothing> {
        val createPostReqDTO: CreatePostReqDTO = PostMapper.mapperToCreatePostReqDTO(createPostReqEntity)
        val response = dataSource.createPost(createPostReqDTO)

        return sendData(response) { BaseMapper.mapperToBaseResEntityVerNothing(response) }
    }

    override suspend fun deletePost(postId: Int): BaseResEntity<Nothing> {
        val response = dataSource.deletePost(postId)

        return sendData(response) { BaseMapper.mapperToBaseResEntity(response) }
    }

    override suspend fun updatePost(postReqEntity: UpdatePostReqEntity): BaseResEntity<UpdatePostResEntity?> {
        val updatePostReqDTO: UpdatePostReqDTO = PostMapper.mapperToUpdatePostReqDTO(postReqEntity)
        val response = dataSource.updatePost(postReqEntity.postId, updatePostReqDTO)

        return sendData(response) { PostMapper.mapperToUpdatePostResEntity(response) }
    }
}