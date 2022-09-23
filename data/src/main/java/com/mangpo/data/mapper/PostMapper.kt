package com.mangpo.data.mapper

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.createPost.CreatePostReqDTO
import com.mangpo.data.model.getPosts.Content
import com.mangpo.data.model.getPosts.GetPostsResDTO
import com.mangpo.data.model.updatePost.UpdatePostReqDTO
import com.mangpo.data.model.updatePost.UpdatePostResDTO
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.createPost.CreatePostReqEntity
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.model.getPosts.GetPostsResEntity
import com.mangpo.domain.model.updatePost.UpdatePostReqEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity

object PostMapper {
    private fun mapperToContentEntities(contentDTOs: List<Content>): List<ContentEntity> {
        val contentEntities: MutableList<ContentEntity> = mutableListOf()

        for (contentDTO in contentDTOs) {
            val contentEntity: ContentEntity = contentDTO.run {
                ContentEntity(id, category, keyword, star, content, createdDate.split("T")[0])
            }

            contentEntities.add(contentEntity)
        }

        return contentEntities
    }

    fun mapperToGetPostsResEntity(getPostsResDTO: BaseResDTO<GetPostsResDTO?>): BaseResEntity<GetPostsResEntity?> {
        return getPostsResDTO.run {
            if (getPostsResDTO.data==null) {
                BaseResEntity(meta.code, meta.msg, null)
            } else {
                BaseResEntity(meta.code, meta.msg, data!!.run {
                    GetPostsResEntity(mapperToContentEntities(content), number, last, numberOfElements, empty)
                })
            }
        }
    }

    fun mapperToCreatePostReqDTO(createPostReqEntity: CreatePostReqEntity): CreatePostReqDTO {
        return createPostReqEntity.run { CreatePostReqDTO(category, content, keyword, star) }
    }

    fun mapperToUpdatePostReqDTO(updatePostReqEntity: UpdatePostReqEntity): UpdatePostReqDTO {
        return updatePostReqEntity.run { UpdatePostReqDTO(category, content, keyword, star) }
    }

    fun mapperToUpdatePostResEntity(updatePostResDTO: BaseResDTO<UpdatePostResDTO?>): BaseResEntity<UpdatePostResEntity?> {
        return updatePostResDTO.run {
            if (data==null) {
                BaseResEntity(meta.code, meta.msg, null)
            } else {
                BaseResEntity(meta.code, meta.msg, data.run { UpdatePostResEntity(id, category, keyword, star, content, createdDate) })
            }
        }
    }
}