package com.mangpo.data.model.getStat

data class GetStatResDTO(
    val countByDayDtoList: List<CountByDayDTO>,
    val countByMonthDtoList: List<CountByMonthDTO>,
    val monthlyCategoryDtoList: List<MonthlyCategoryDTO>,
    val percentageOfCategory: PercentageOfCategoryDTO,
    val cntOfCategory: CntOfCategoryDTO,
    val totalPost: Int
)