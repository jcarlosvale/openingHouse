package com.wolt.api.service

import com.wolt.api.dtos.BusinessHoursDto
import com.wolt.api.dtos.HourEventDto
import com.wolt.api.enums.EventType
import com.wolt.api.exceptions.ParserException
import org.junit.Assert.assertEquals
import org.junit.Test

class WoltServiceTest {

    private val woltService = WoltService()

    @Test
    fun `Test success process` () {

        val expectedReturn: String =
                "Monday: 10 AM - 8 PM\n" +
                "Tuesday: Closed\n" +
                "Wednesday: Closed\n" +
                "Thursday: Closed\n" +
                "Friday: 10 AM - 6 PM\n" +
                "Saturday: 6 PM - 1 AM\n" +
                "Sunday: 9 AM - 11 AM, 4 PM - 11 PM"

        val businessHoursDto = BusinessHoursDto(
                listOf(
                        HourEventDto(EventType.OPEN, 36000),
                        HourEventDto(EventType.CLOSE, 72000)
                ),
                listOf(),
                listOf(),
                listOf(),
                listOf(
                        HourEventDto(EventType.OPEN, 36000),
                        HourEventDto(EventType.CLOSE, 64800)
                ),
                listOf(HourEventDto(EventType.OPEN, 64800)),
                listOf(
                        HourEventDto(EventType.CLOSE, 3600),
                        HourEventDto(EventType.OPEN, 32400),
                        HourEventDto(EventType.CLOSE, 39600),
                        HourEventDto(EventType.OPEN, 57600),
                        HourEventDto(EventType.CLOSE, 82800)
                )
        )

        val actualReturn: String = woltService.process(businessHoursDto)

        assertEquals(expectedReturn, actualReturn)
    }

    @Test
    fun `Test empty business hour process` () {

        val expectedReturn: String =
                        "Monday: Closed\n" +
                        "Tuesday: Closed\n" +
                        "Wednesday: Closed\n" +
                        "Thursday: Closed\n" +
                        "Friday: Closed\n" +
                        "Saturday: Closed\n" +
                        "Sunday: Closed"

        val businessHoursDto = BusinessHoursDto(
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf()
        )

        val actualReturn: String = woltService.process(businessHoursDto)

        assertEquals(expectedReturn, actualReturn)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid hour process`() {
        val businessHoursDto = BusinessHoursDto(
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(HourEventDto(EventType.OPEN,-1)),
                listOf(),
                listOf()
        )
        woltService.process(businessHoursDto)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid open close process`() {
        val businessHoursDto = BusinessHoursDto(
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(
                        HourEventDto(EventType.OPEN,36000),
                        HourEventDto(EventType.OPEN,64800)
                ),
                listOf(),
                listOf()
        )
        woltService.process(businessHoursDto)
    }

    @Test(expected = ParserException::class)
    fun `Test invalid working day process`() {
        val businessHoursDto = BusinessHoursDto(
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(HourEventDto(EventType.OPEN,36000)),
                listOf(),
                listOf()
        )
        woltService.process(businessHoursDto)
    }

}