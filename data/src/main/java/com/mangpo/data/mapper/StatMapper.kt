package com.mangpo.data.mapper

import com.mangpo.data.model.base.BaseResDTO
import com.mangpo.data.model.getStat.CountByDayDTO
import com.mangpo.data.model.getStat.CountByMonthDTO
import com.mangpo.data.model.getStat.GetStatResDTO
import com.mangpo.data.model.getStat.MonthlyCategoryDTO
import com.mangpo.domain.model.base.BaseResEntity
import com.mangpo.domain.model.getStat.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object StatMapper {
    fun mapperToGetStatResEntity(getStatResDTO: BaseResDTO<GetStatResDTO>): BaseResEntity<GetStatResEntity> {
        return getStatResDTO.run {
            BaseResEntity<GetStatResEntity>(
                meta.code,
                meta.msg,
                data?.run {
                    GetStatResEntity(
                        totalPost,
                        percentageOfCategory.run {
                            PercentageOfCategoryEntity(AMBIGUOUS, HEARING, SIGHT, SMELL, TASTE, TOUCH)
                        },
                        mapperToMonthlyCategoryEntities(monthlyCategoryDtoList),
                        mapperToCountByDayEntities(countByDayDtoList),
                        mapperToCountByMonthEntities(countByMonthDtoList)
                    )
                }
            )
        }
    }

    private fun mapperToMonthlyCategoryEntities(monthlyCategoryDTOs: List<MonthlyCategoryDTO>): List<MonthlyCategoryEntity> {
        val monthlyCategoryEntities: MutableList<MonthlyCategoryEntity> = mutableListOf()

        for (monthlyCategoryDTO in monthlyCategoryDTOs.reversed()) {
            monthlyCategoryEntities.add(monthlyCategoryDTO.run {
                MonthlyCategoryEntity(category, "${LocalDate.parse(month).monthValue}월")
            })
        }

        return monthlyCategoryEntities
    }

    private fun mapperToCountByDayEntities(countByDayDTOs: List<CountByDayDTO>): List<CountByDayEntity> {
        val countByDayEntities: MutableList<CountByDayEntity> = mutableListOf()

        for (countByDayDTO in countByDayDTOs) {
            countByDayEntities.add(countByDayDTO.run {
                CountByDayEntity(count, LocalDate.parse(day).format(DateTimeFormatter.ofPattern("MM/dd")))
            })
        }

        return countByDayEntities
    }

    private fun mapperToCountByMonthEntities(countByMonthDTOs: List<CountByMonthDTO>): List<CountByMonthEntity> {
        val countByMonthEntities: MutableList<CountByMonthEntity> = mutableListOf()

        for (countByMonthDTO in countByMonthDTOs) {
            countByMonthEntities.add(countByMonthDTO.run {
                CountByMonthEntity(count, "${LocalDate.parse(month).monthValue}월")
            })
        }

        return countByMonthEntities
    }
}