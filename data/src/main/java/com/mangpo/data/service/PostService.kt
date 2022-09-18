package com.mangpo.data.service

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createPost.CreatePostReqDTO
import com.mangpo.data.model.createPost.CreatePostResDTO
import com.mangpo.data.model.getPosts.GetPostsResDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostService {
    @GET("/api/posts?size=20")
    suspend fun getPosts(@Query("userId") userId: Int, @Query("page") page: Int, @Query("sort") sort: String, @Query("createdDate") createdDate: String?, @Query("star") star: Int?, @Query("category") category: String?): BaseResDTO<GetPostsResDTO?>

    @POST("/api/posts")
    suspend fun createPost(@Body createPostReqDTO: CreatePostReqDTO): BaseResDTO<CreatePostResDTO?>
}