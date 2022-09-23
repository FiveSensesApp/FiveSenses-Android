package com.mangpo.domain.repository

import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.createPost.CreatePostReqEntity
import com.mangpo.domain.model.getPosts.GetPostsResEntity
import com.mangpo.domain.model.updatePost.UpdatePostReqEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity

interface PostRepository {
    suspend fun getPosts(userId: Int, page: Int, sort: String, createDate: String?, star: Int?, category: String?): BaseResEntity<GetPostsResEntity?>
    suspend fun createPost(createPostReqEntity: CreatePostReqEntity): BaseResEntity<Nothing>
    suspend fun deletePost(postId: Int): BaseResEntity<Nothing>
    suspend fun updatePost(postReqEntity: UpdatePostReqEntity): BaseResEntity<UpdatePostResEntity?>
}