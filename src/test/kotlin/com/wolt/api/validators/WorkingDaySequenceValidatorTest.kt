package com.wolt.api.validators

import com.wolt.api.domain.BusinessHours
import com.wolt.api.domain.HourEvent
import com.wolt.api.enums.EventType
import com.wolt.api.exceptions.ParserException
import org.junit.Test
import java.time.DayOfWeek

class WorkingDaySequenceValidatorTest {

    @Test
    fun `Test valid empty work day sequence`() {

        val businessHours = BusinessHours()

        WorkingDaySequenceValidator().validate(businessHours)
    }

    @Test
    fun `Test valid work day sequence`() {

        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf(
                HourEvent(EventType.CLOSE, 1),
                HourEvent(EventType.OPEN, 2),
                HourEvent(EventType.CLOSE, 3)))

        businessHours.setBusinessHours(DayOfWeek.WEDNESDAY, mutableListOf(
                HourEvent(EventType.OPEN, 1),
                HourEvent(EventType.CLOSE, 2),
                HourEvent(EventType.OPEN, 3)))

        businessHours.setBusinessHours(DayOfWeek.THURSDAY, mutableListOf(
                HourEvent(EventType.CLOSE, 5),
                HourEvent(EventType.OPEN, 6),
                HourEvent(EventType.CLOSE, 7),
                HourEvent(EventType.OPEN, 8),
                HourEvent(EventType.CLOSE, 9)))

        businessHours.setBusinessHours(DayOfWeek.SUNDAY, mutableListOf(
                HourEvent(EventType.OPEN, 8),
                HourEvent(EventType.CLOSE, 9),
                HourEvent(EventType.OPEN, 11)))

        WorkingDaySequenceValidator().validate(businessHours)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid work day sequence close without open`() {

        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf(
                HourEvent(EventType.CLOSE, 1),
                HourEvent(EventType.OPEN, 2),
                HourEvent(EventType.CLOSE, 3)))

        businessHours.setBusinessHours(DayOfWeek.SUNDAY, mutableListOf(
                HourEvent(EventType.OPEN, 8),
                HourEvent(EventType.CLOSE, 11)))

        WorkingDaySequenceValidator().validate(businessHours)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid work day sequence close without previous working day`() {

        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf(
                HourEvent(EventType.CLOSE, 1),
                HourEvent(EventType.OPEN, 2),
                HourEvent(EventType.CLOSE, 3)))

        WorkingDaySequenceValidator().validate(businessHours)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid work day sequence open without close`() {

        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf(
                HourEvent(EventType.OPEN, 2),
                HourEvent(EventType.CLOSE, 3)))

        businessHours.setBusinessHours(DayOfWeek.SUNDAY, mutableListOf(
                HourEvent(EventType.OPEN, 8),
                HourEvent(EventType.CLOSE, 11),
                HourEvent(EventType.OPEN, 12)))

        WorkingDaySequenceValidator().validate(businessHours)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid work day sequence open without next working day`() {

        val businessHours = BusinessHours()
        businessHours.setBusinessHours(DayOfWeek.MONDAY, mutableListOf(
                HourEvent(EventType.OPEN, 1),
                HourEvent(EventType.CLOSE, 2),
                HourEvent(EventType.OPEN, 3)))

        WorkingDaySequenceValidator().validate(businessHours)
    }
}