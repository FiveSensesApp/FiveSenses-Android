package com.mangpo.data.service

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createPost.CreatePostReqDTO
import com.mangpo.data.model.createPost.CreatePostResDTO
import com.mangpo.data.model.getPosts.GetPostsResDTO
import com.mangpo.data.model.getPresentPostsBetween.GetPresentPostsBetweenResDTO
import com.mangpo.data.model.searchKeywordLike.SearchKeywordLikeResDTO
import com.mangpo.data.model.updatePost.UpdatePostReqDTO
import com.mangpo.data.model.updatePost.UpdatePostResDTO
import retrofit2.http.*

interface PostService {
    @GET("/api/posts?size=20")
    suspend fun getPosts(@Query("userId") userId: Int, @Query("page") page: Int, @Query("sort") sort: String, @Query("createdDate") createdDate: String?, @Query("star") star: Int?, @Query("category") category: String?): BaseResDTO<GetPostsResDTO?>

    @GET("/api/posts/count")
    suspend fun findCountByParam(@Query("userId") userId: Int, @Query("category") category: String?, @Query("star") star: Int?, @Query("createdDate") createdDate: String?): BaseResDTO<Int>

    @GET("/api/posts/present-between")
    suspend fun getPresentPostsBetween(@Query("startDate") startDate: String, @Query("endDate") endDate: String): BaseResDTO<List<GetPresentPostsBetweenResDTO>>

    @GET("/api/posts/search-keyword")
    suspend fun searchKeywordLike(@Query("query") query: String): BaseResDTO<List<SearchKeywordLikeResDTO>?>

    @POST("/api/posts")
    suspend fun createPost(@Body createPostReqDTO: CreatePostReqDTO): BaseResDTO<CreatePostResDTO?>

    @PATCH("/api/posts/{postId}")
    suspend fun updatePost(@Body updatePostReqDTO: UpdatePostReqDTO, @Path("postId") postId: Int): BaseResDTO<UpdatePostResDTO?>

    @DELETE("/api/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Int): BaseResDTO<Nothing>
}