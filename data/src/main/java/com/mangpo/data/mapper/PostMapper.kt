package com.mangpo.data.mapper

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getPosts.Content
import com.mangpo.data.model.getPosts.GetPostsResDTO
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.model.getPosts.GetPostsResEntity
import java.text.SimpleDateFormat
import java.util.*

object PostMapper {
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

    private fun mapperToContentEntities(contentDTOs: List<Content>): List<ContentEntity> {
        val contentEntities: MutableList<ContentEntity> = mutableListOf()

        for (contentDTO in contentDTOs) {
            val contentEntity: ContentEntity = contentDTO.run {
                ContentEntity(id, category, keyword, star, content, SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN).format(createdDate))
            }

            contentEntities.add(contentEntity)
        }

        return contentEntities
    }
}