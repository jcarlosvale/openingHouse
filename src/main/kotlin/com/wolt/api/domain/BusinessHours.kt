package com.wolt.api.domain

import com.wolt.api.enums.EventType
import java.time.DayOfWeek

class BusinessHours {

    private val mapWeekBusinessHour : MutableMap<DayOfWeek, MutableList<HourEvent>> = mutableMapOf()

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
}
