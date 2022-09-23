package com.mangpo.domain.model.getPosts

data class GetPostsResEntity(
    val content: List<ContentEntity>,
    val pageNumber: Int,
    val isLast: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
