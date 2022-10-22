package com.mangpo.domain.model.getStat

data class GetStatResEntity(
    val totalPost: Int,
    val percentageOfCategory: PercentageOfCategoryEntity,
    val monthlyCategoryEntities: List<MonthlyCategoryEntity>,
    val countByDayEntities: List<CountByDayEntity>,
    val countByMonthEntities: List<CountByMonthEntity>,
    val cntOfCategory: PercentageOfCategoryEntity
)