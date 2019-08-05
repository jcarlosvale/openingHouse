package com.wolt.api.validators

import com.wolt.api.domain.BusinessHours
import com.wolt.api.domain.HourEvent
import com.wolt.api.enums.EventType
import com.wolt.api.exceptions.ParserException
import org.junit.Test
import java.time.DayOfWeek

class OpenCloseHoursSequenceValidatorTest {

    @Test
    fun `Test valid open close sequence`() {

        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf(HourEvent(EventType.OPEN, 0)))
        businessHours.setBusinessHours(DayOfWeek.TUESDAY, mutableListOf(HourEvent(EventType.CLOSE, 1)))

        businessHours.setBusinessHours(DayOfWeek.WEDNESDAY, mutableListOf(
                HourEvent(EventType.OPEN, 2),
                HourEvent(EventType.CLOSE, 3),
                HourEvent(EventType.OPEN, 4)))

        businessHours.setBusinessHours(DayOfWeek.THURSDAY, mutableListOf(
                HourEvent(EventType.CLOSE, 5),
                HourEvent(EventType.OPEN, 6),
                HourEvent(EventType.CLOSE, 7)))

        businessHours.setBusinessHours(DayOfWeek.FRIDAY, mutableListOf())
        businessHours.setBusinessHours(DayOfWeek.SATURDAY, mutableListOf())
        businessHours.setBusinessHours(DayOfWeek.SUNDAY, mutableListOf(
                HourEvent(EventType.OPEN, 8),
                HourEvent(EventType.CLOSE, 9),
                HourEvent(EventType.OPEN, 10),
                HourEvent(EventType.CLOSE, 11)))

        OpenCloseHoursSequenceValidator().validate(businessHours)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid close sequence`() {
        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.WEDNESDAY, mutableListOf(
                HourEvent(EventType.CLOSE, 2),
                HourEvent(EventType.CLOSE, 4)))
        OpenCloseHoursSequenceValidator().validate(businessHours)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid open sequence`() {
        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.WEDNESDAY, mutableListOf(
                HourEvent(EventType.OPEN, 2),
                HourEvent(EventType.OPEN, 4)))
        OpenCloseHoursSequenceValidator().validate(businessHours)
    }

}