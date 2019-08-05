package com.wolt.api.validators

import com.wolt.api.domain.BusinessHours
import com.wolt.api.domain.HourEvent
import com.wolt.api.enums.EventType
import com.wolt.api.exceptions.ParserException
import java.time.DayOfWeek

/**
 * Verify if one working day NOT contains two OPEN or CLOSE event type sequence in a Business Hours object
 */
class OpenCloseHoursSequenceValidator : BusinessHoursValidator<BusinessHours> {

    override fun validate(businessHours: BusinessHours) {

        for (dayOfWeek : DayOfWeek in DayOfWeek.values()) {

            if (!businessHours.isWorkingDay(dayOfWeek)) continue

            var previousEventType: EventType = businessHours.startEventType(dayOfWeek)

            val listOfHourEvent: MutableList<HourEvent> = businessHours.getListOfHourEvent(dayOfWeek)

            for (index: Int in 1 until (listOfHourEvent.size)) {

                val hourEvent : HourEvent = listOfHourEvent[index]

                if (previousEventType == hourEvent.type)
                    throw ParserException("Missing correct OPEN/CLOSE sequence in $dayOfWeek.")

                previousEventType = hourEvent.type
            }

        }

    }
}