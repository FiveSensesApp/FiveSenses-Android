package com.mangpo.taste.view.model

import com.mangpo.domain.model.RecordEntity

data class Record (
    val viewType: Int = 0,
    val record: RecordEntity? = null
)