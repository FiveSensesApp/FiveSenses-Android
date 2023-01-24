package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createPost.CreatePostReqDTO
import com.mangpo.data.model.createPost.CreatePostResDTO
import com.mangpo.data.model.getPosts.GetPostsResDTO
import com.mangpo.data.model.getPresentPostsBetween.GetPresentPostsBetweenResDTO
import com.mangpo.data.model.searchKeywordLike.SearchKeywordLikeResDTO
import com.mangpo.data.model.updatePost.UpdatePostReqDTO
import com.mangpo.data.model.updatePost.UpdatePostResDTO
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

    override suspend fun findCountByParam(
        userId: Int,
        category: String?,
        star: Int?,
        createdDate: String?
    ): BaseResDTO<Int> {
        return callApi { service.findCountByParam(userId, category, star, createdDate) }
    }

    override suspend fun getPresentPostsBetween(
        startDate: String,
        endDate: String
    ): BaseResDTO<List<GetPresentPostsBetweenResDTO>> {
        return callApi { service.getPresentPostsBetween(startDate, endDate) }
    }

    override suspend fun searchKeywordLike(query: String): BaseResDTO<List<SearchKeywordLikeResDTO>?> {
        return callApi { service.searchKeywordLike(query) }
    }

    override suspend fun createPost(createPostReqDTO: CreatePostReqDTO): BaseResDTO<CreatePostResDTO?> {
        return callApi { service.createPost(createPostReqDTO) }
    }

    override suspend fun deletePost(postId: Int): BaseResDTO<Nothing> {
        return callApi { service.deletePost(postId) }
    }

    override suspend fun updatePost(
        postId: Int,
        updatePostReqDTO: UpdatePostReqDTO
    ): BaseResDTO<UpdatePostResDTO?> {
        return callApi { service.updatePost(updatePostReqDTO, postId) }
    }
}