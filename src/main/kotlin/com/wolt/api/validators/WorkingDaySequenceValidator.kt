package com.wolt.api.validators

import com.wolt.api.domain.BusinessHours
import com.wolt.api.enums.EventType
import com.wolt.api.exceptions.ParserException
import java.time.DayOfWeek

/**
 * Verify if the working days sequence doesn't contains:
 * - One working day starting with one close event and the previous day is not a working day OR ends with a START event.
 * - One working day not closed and the following day is not a working day OR starts with a CLOSE event
 */
class WorkingDaySequenceValidator : BusinessHoursValidator<BusinessHours> {

    override fun validate(businessHours: BusinessHours) {
        for (dayOfWeek : DayOfWeek in DayOfWeek.values()) {

            if (!businessHours.isWorkingDay(dayOfWeek)) continue

            if (businessHours.dayStartsWithEventType(dayOfWeek, EventType.CLOSE)) {

                val previousDayOfWeek: DayOfWeek = dayOfWeek.minus(1)

                if (!businessHours.isWorkingDay(previousDayOfWeek))
                    throw ParserException("$previousDayOfWeek should be a working day.")

                if (businessHours.dayEndsWithEventType(previousDayOfWeek, EventType.CLOSE)) {
                    throw ParserException("The last hour event type of $previousDayOfWeek must be an OPEN event.")
                }
            }

            if (businessHours.dayEndsWithEventType(dayOfWeek, EventType.OPEN)) {
                val nextDayOfWeek: DayOfWeek = dayOfWeek.plus(1)

                if (!businessHours.isWorkingDay(nextDayOfWeek))
                    throw ParserException("$nextDayOfWeek must be a working day.")

                if (businessHours.dayStartsWithEventType(nextDayOfWeek, EventType.OPEN)) {
                    throw ParserException("The first hour event type of $nextDayOfWeek must be an CLOSE event.")
                }
            }
        }
    }
}
