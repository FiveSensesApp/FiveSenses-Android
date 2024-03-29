package com.mangpo.domain.model.getPosts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentEntity(
    val id: Int,
    val category: String,
    val keyword: String,
    val star: Int,
    val content: String?,
    val createdDate: String
) : Parcelable
