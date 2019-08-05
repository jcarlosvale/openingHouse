package com.wolt.api.validators

import com.wolt.api.domain.BusinessHours
import com.wolt.api.domain.HourEvent
import com.wolt.api.enums.EventType
import com.wolt.api.exceptions.ParserException
import org.junit.Test
import java.time.DayOfWeek

class HourRangeValidatorTest {

    @Test
    fun `Test valid hour range`() {

        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf(HourEvent(EventType.OPEN, 0)))
        businessHours.setBusinessHours(DayOfWeek.TUESDAY, mutableListOf(HourEvent(EventType.CLOSE, 86399)))
        businessHours.setBusinessHours(DayOfWeek.WEDNESDAY, mutableListOf(HourEvent(EventType.OPEN, 32400)))
        businessHours.setBusinessHours(DayOfWeek.THURSDAY, mutableListOf(HourEvent(EventType.CLOSE, 37800)))
        businessHours.setBusinessHours(DayOfWeek.FRIDAY, mutableListOf(HourEvent(EventType.OPEN, 75600)))
        businessHours.setBusinessHours(DayOfWeek.SATURDAY, mutableListOf(HourEvent(EventType.CLOSE, 0)))
        businessHours.setBusinessHours(DayOfWeek.SUNDAY, mutableListOf(HourEvent(EventType.OPEN, 0)))

        HourRangeValidator().validate(businessHours)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid hour inferior bound range`() {
        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.FRIDAY, mutableListOf(HourEvent(EventType.OPEN, -1)))
        HourRangeValidator().validate(businessHours)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid hour superior bound range`() {
        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.SUNDAY, mutableListOf(HourEvent(EventType.OPEN, 86400)))
        HourRangeValidator().validate(businessHours)
    }
}