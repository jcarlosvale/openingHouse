package com.wolt.api.converter

import com.wolt.api.domain.BusinessHours
import com.wolt.api.domain.HourEvent
import com.wolt.api.dtos.BusinessHoursDto
import com.wolt.api.dtos.HourEventDto
import com.wolt.api.enums.EventType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek

class BusinessHourConverterTest {

    @Test
    fun `Test sorted success converter` () {
        val businessHourConverter = BusinessHourConverter()

        val businessHoursDto = BusinessHoursDto(
                emptyList(),
                emptyList(),
                listOf(
                        HourEventDto(EventType.OPEN, 10),
                        HourEventDto(EventType.OPEN, 5),
                        HourEventDto(EventType.CLOSE, 0)),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
        )

        val expectedBusinessHours = BusinessHours()
        expectedBusinessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.TUESDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.WEDNESDAY,
                mutableListOf(
                        HourEvent(EventType.CLOSE, 0),
                        HourEvent(EventType.OPEN, 5),
                        HourEvent(EventType.OPEN, 10)))
        expectedBusinessHours.setBusinessHours(DayOfWeek.THURSDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.FRIDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.SATURDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.SUNDAY, mutableListOf())

        val actualBusinessHours: BusinessHours = businessHourConverter.convert(businessHoursDto)

        assertEquals(expectedBusinessHours, actualBusinessHours)
    }

    @Test
    fun `Test empty values converter` () {
        val businessHourConverter = BusinessHourConverter()

        val businessHoursDto = BusinessHoursDto(
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
        )

        val expectedBusinessHours = BusinessHours()
        expectedBusinessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.TUESDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.WEDNESDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.THURSDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.FRIDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.SATURDAY, mutableListOf())
        expectedBusinessHours.setBusinessHours(DayOfWeek.SUNDAY, mutableListOf())

        val actualBusinessHours: BusinessHours = businessHourConverter.convert(businessHoursDto)

        assertEquals(expectedBusinessHours, actualBusinessHours)
    }

    @Test
    fun `Test full values converter` () {
        val businessHourConverter = BusinessHourConverter()

        val businessHoursDto = BusinessHoursDto(
                listOf(HourEventDto(EventType.OPEN, 1)),
                listOf(HourEventDto(EventType.CLOSE, 2)),
                listOf(HourEventDto(EventType.OPEN, 3)),
                listOf(HourEventDto(EventType.CLOSE, 4)),
                listOf(HourEventDto(EventType.OPEN, 5)),
                listOf(HourEventDto(EventType.CLOSE, 6)),
                listOf(HourEventDto(EventType.OPEN, 7))
        )

        val expectedBusinessHours = BusinessHours()
        expectedBusinessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf(HourEvent(EventType.OPEN, 1)))
        expectedBusinessHours.setBusinessHours(DayOfWeek.TUESDAY, mutableListOf(HourEvent(EventType.CLOSE, 2)))
        expectedBusinessHours.setBusinessHours(DayOfWeek.WEDNESDAY, mutableListOf(HourEvent(EventType.OPEN, 3)))
        expectedBusinessHours.setBusinessHours(DayOfWeek.THURSDAY, mutableListOf(HourEvent(EventType.CLOSE, 4)))
        expectedBusinessHours.setBusinessHours(DayOfWeek.FRIDAY, mutableListOf(HourEvent(EventType.OPEN, 5)))
        expectedBusinessHours.setBusinessHours(DayOfWeek.SATURDAY, mutableListOf(HourEvent(EventType.CLOSE, 6)))
        expectedBusinessHours.setBusinessHours(DayOfWeek.SUNDAY, mutableListOf(HourEvent(EventType.OPEN, 7)))

        val actualBusinessHours: BusinessHours = businessHourConverter.convert(businessHoursDto)

        assertEquals(expectedBusinessHours, actualBusinessHours)
    }
}