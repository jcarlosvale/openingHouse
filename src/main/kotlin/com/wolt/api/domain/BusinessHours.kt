package com.wolt.api.domain

import com.wolt.api.dtos.HourEventDto
import com.wolt.api.enums.EventType
import java.time.DayOfWeek

/**
 * Domain class for BusinessHours used in the Processing Logic
 * Represents the WEEK Business Hour of a restaurant
 */
class BusinessHours {

    private val mapWeekBusinessHour : MutableMap<DayOfWeek, MutableList<HourEvent>> = mutableMapOf()

    init {
        mapWeekBusinessHour[DayOfWeek.MONDAY] = mutableListOf()
        mapWeekBusinessHour[DayOfWeek.TUESDAY] = mutableListOf()
        mapWeekBusinessHour[DayOfWeek.WEDNESDAY] = mutableListOf()
        mapWeekBusinessHour[DayOfWeek.THURSDAY] = mutableListOf()
        mapWeekBusinessHour[DayOfWeek.FRIDAY] = mutableListOf()
        mapWeekBusinessHour[DayOfWeek.SATURDAY] = mutableListOf()
        mapWeekBusinessHour[DayOfWeek.SUNDAY] = mutableListOf()
    }

    fun setBusinessHours(dayOfWeek: DayOfWeek, listHourEvent: MutableList<HourEvent>) {
        mapWeekBusinessHour[dayOfWeek] = listHourEvent
    }

    fun isWorkingDay(dayOfWeek: DayOfWeek): Boolean {
        return mapWeekBusinessHour.getValue(dayOfWeek).isNotEmpty()
    }

    fun dayStartsWithEventType(dayOfWeek: DayOfWeek, eventType: EventType): Boolean {
        return startEventType(dayOfWeek) == eventType
    }

    fun dayEndsWithEventType(dayOfWeek: DayOfWeek, eventType: EventType): Boolean {
        return mapWeekBusinessHour.getValue(dayOfWeek).last().type  == eventType
    }

    fun startEventType(dayOfWeek: DayOfWeek): EventType {
        return mapWeekBusinessHour.getValue(dayOfWeek).first().type
    }

    fun getListOfHourEvent(dayOfWeek: DayOfWeek): MutableList<HourEvent> {
        return mapWeekBusinessHour.getValue(dayOfWeek)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BusinessHours

        if (mapWeekBusinessHour != other.mapWeekBusinessHour) return false

        return true
    }

    override fun hashCode(): Int {
        return mapWeekBusinessHour.hashCode()
    }

    override fun toString(): String {
        return "BusinessHours(mapWeekBusinessHour=$mapWeekBusinessHour)"
    }

}
