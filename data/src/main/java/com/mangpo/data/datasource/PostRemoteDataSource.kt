package com.mangpo.data.datasource

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createPost.CreatePostReqDTO
import com.mangpo.data.model.createPost.CreatePostResDTO
import com.mangpo.data.model.getPosts.GetPostsResDTO
import com.mangpo.data.model.updatePost.UpdatePostReqDTO
import com.mangpo.data.model.updatePost.UpdatePostResDTO

interface PostRemoteDataSource {
    suspend fun getPosts(userId: Int, page: Int, sort: String, createDate: String?, star: Int?, category: String?): BaseResDTO<GetPostsResDTO?>
    suspend fun createPost(createPostReqDTO: CreatePostReqDTO): BaseResDTO<CreatePostResDTO?>
    suspend fun deletePost(postId: Int): BaseResDTO<Nothing>
    suspend fun updatePost(postId: Int, updatePostReqDTO: UpdatePostReqDTO): BaseResDTO<UpdatePostResDTO?>
}