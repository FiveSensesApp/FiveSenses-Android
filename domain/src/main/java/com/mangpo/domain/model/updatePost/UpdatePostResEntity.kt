package com.mangpo.domain.model.updatePost

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdatePostResEntity(
    val id: Int,
    val category: String,
    val keyword: String,
    val star: Int,
    val content: String?,
    val createdDate: String
) : Parcelable