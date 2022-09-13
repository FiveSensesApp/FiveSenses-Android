package com.mangpo.data.util

import java.text.SimpleDateFormat
import java.util.*

object MapperUtils {
    fun calDate(createdDate: String): Int = ((Calendar.getInstance().time.time - SimpleDateFormat("yyyy-MM-dd").parse(createdDate).time) / (24 * 60 * 60 * 1000)).toInt()
}