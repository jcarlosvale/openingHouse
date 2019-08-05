package com.wolt.api.domain

import com.wolt.api.enums.EventType
import org.junit.Test

import org.junit.Assert.*

class HourEventTest {

    @Test
    fun `Test Unix 12-Hour Format`() {
        assertEquals("12 AM", HourEvent(EventType.OPEN, 0).getHourFromUnixTimeStampString())
        assertEquals("11 PM", HourEvent(EventType.OPEN, 86399).getHourFromUnixTimeStampString())
        assertEquals("9 AM", HourEvent(EventType.OPEN, 32400).getHourFromUnixTimeStampString())
        assertEquals("10 AM", HourEvent(EventType.OPEN, 37800).getHourFromUnixTimeStampString())
        assertEquals("9 PM", HourEvent(EventType.OPEN, 75600).getHourFromUnixTimeStampString())
    }
}