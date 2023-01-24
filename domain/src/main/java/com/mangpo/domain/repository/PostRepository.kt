package com.mangpo.domain.repository

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.createPost.CreatePostReqEntity
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.model.getPosts.GetPostsResEntity
import com.mangpo.domain.model.getPresentPostsBetween.GetPresentPostsBetweenResEntity
import com.mangpo.domain.model.updatePost.UpdatePostReqEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity

interface PostRepository {
    suspend fun getPosts(userId: Int, page: Int, sort: String, createDate: String?, star: Int?, category: String?): BaseResEntity<GetPostsResEntity?>
    suspend fun findCountByParam(userId: Int, category: String?, star: Int?, createdDate: String?): BaseResEntity<Int>
    suspend fun getPresentPostsBetween(startDate: String, endDate: String): BaseResEntity<List<GetPresentPostsBetweenResEntity>>
    suspend fun searchKeywordLike(query: String): BaseResEntity<List<ContentEntity>>
    suspend fun createPost(createPostReqEntity: CreatePostReqEntity): BaseResEntity<Nothing>
    suspend fun deletePost(postId: Int): BaseResEntity<Nothing>
    suspend fun updatePost(postReqEntity: UpdatePostReqEntity): BaseResEntity<UpdatePostResEntity?>
}