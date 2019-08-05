package com.wolt.api.converter

import com.wolt.api.domain.BusinessHours
import com.wolt.api.domain.HourEvent
import com.wolt.api.dtos.BusinessHoursDto
import com.wolt.api.dtos.HourEventDto
import java.time.DayOfWeek

/**
 * Converter DTO Business Hour --> Domain Object Business Hour
 */
class BusinessHourConverter {

    fun convert(businessHoursDto: BusinessHoursDto): BusinessHours {

        val businessHours = BusinessHours()

        businessHours.setBusinessHours(DayOfWeek.MONDAY,convertListToSortedMutableList(businessHoursDto.monday))
        businessHours.setBusinessHours(DayOfWeek.TUESDAY,convertListToSortedMutableList(businessHoursDto.tuesday))
        businessHours.setBusinessHours(DayOfWeek.WEDNESDAY,convertListToSortedMutableList(businessHoursDto.wednesday))
        businessHours.setBusinessHours(DayOfWeek.THURSDAY,convertListToSortedMutableList(businessHoursDto.thursday))
        businessHours.setBusinessHours(DayOfWeek.FRIDAY,convertListToSortedMutableList(businessHoursDto.friday))
        businessHours.setBusinessHours(DayOfWeek.SATURDAY,convertListToSortedMutableList(businessHoursDto.saturday))
        businessHours.setBusinessHours(DayOfWeek.SUNDAY,convertListToSortedMutableList(businessHoursDto.sunday))

        return businessHours
    }

    private fun convertListToSortedMutableList(listOfHourEventDto: List<HourEventDto>): MutableList<HourEvent> {

        val mutableListOfHourEvent: MutableList<HourEvent> = mutableListOf()

        listOfHourEventDto.forEach { mutableListOfHourEvent.add(HourEvent(it.type, it.value)) }

        mutableListOfHourEvent.sortBy { it.value }

        return mutableListOfHourEvent
    }

}
